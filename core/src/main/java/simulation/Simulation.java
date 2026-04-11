package simulation;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.CRC32;

import io.netty.buffer.ByteBuf;
import simulation.GameObjects.GameObject;
import simulation.GameObjects.ObjectDecoder;
import simulation.GameObjects.ObjectEncoder;
import simulation.GameObjects.Troops.Archer;
import simulation.GameObjects.Troops.Dragon;
import simulation.GameObjects.Troops.Knight;
import simulation.GameObjects.Troops.Mage;
import simulation.GameObjects.Troops.Tower;
import simulation.GameObjects.Troops.Troop;
import simulation.ScheduledActions.ActionDecoder;
import simulation.ScheduledActions.ActionEncoder;
import simulation.ScheduledActions.ScheduledAction;

public class Simulation {
    private int tick = 0;
    private int currentObjId;

    private final SimPlayer[] players = new SimPlayer[2];
    private final List<GameObject> gameObjects = new ArrayList<>();
    private Tower[] towerList = new Tower[2];
    private final Map<Integer, List<ScheduledAction>> actionQueue = new HashMap<>();
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();

    private final CRC32 crc32 = new CRC32();
    private final ByteBuffer buffer = ByteBuffer.allocate(12);
    private static final int HISTORY_SIZE = 10;
    private final long[] checksumHistory = new long[HISTORY_SIZE];

    private long currentChecksum;
    private boolean isServer;

    public Simulation(boolean isServer) {
        players[0] = new SimPlayer(0);
        players[1] = new SimPlayer(1);
        towerList[0] = new Tower(4, 100, 50,  0);
        towerList[1] = new Tower(4, 1440, 50, 1);

        towerList[0].setId(currentObjId++);
        this.addObject(towerList[0]);


        towerList[1].setId(currentObjId++);
        this.addObject(towerList[1]);

        this.isServer = isServer;
    }

    public void setTarget() {
        for(GameObject gameObject : gameObjects) {
            for (GameObject gameObject2 : gameObjects) {
                if (gameObject instanceof Troop && gameObject2 instanceof Troop) {
                    Troop troop1 = (Troop) gameObject;
                    if (troop1.getTarget() != null && troop1.getTarget().isDead()) {
                        troop1.setTarget(null);
                    }
                    Troop troop2 = (Troop) gameObject2;

                    if (troop1 == troop2 || troop1.getTeam() == troop2.getTeam()) {
                        continue;
                    } else {
                        if (troop1.canAttack(troop2)) {
                            if (troop1.getTarget() == null) {
                                troop1.setTarget(troop2);
                            } else {
                                int distance = troop1.calculateDistance(troop2); 
                                if (troop1.calculateDistance(troop1.getTarget()) > distance) {
                                    troop1.setTarget(troop2);
                                } 
                            }
                        }
                    }
                }
            }
        }
    }

    public List<GameObject> getObjects() {
        return gameObjects;
    }

    public boolean isGameOver() {
        for(int i = 0; i < 2; i++) {
            if (towerList[i].isDead()) {
                return true;
            }
        }
        return false;
    }

    public int getWinner() {
        if (towerList[0].isDead()) return 1;
        else if (towerList[1].isDead()) return 0;
        else return -1;
    }

    public void update() {
        
        if (!isGameOver()) {
            processNetworkTasks();
            this.tick++;

            //for interpolation
            if (!isServer) {
                for (GameObject gameObject : gameObjects) {
                    gameObject.savePreviousState();
                }
            }

            if(this.tick % 20 == 0) {
                players[0].addGold(100);
                players[1].addGold(100);
            }

            setTarget();
            ArrayList<Troop> toRemove = new ArrayList<>();

            for(GameObject gameObject : gameObjects) {
                if (gameObject instanceof Troop) {
                    if(((Troop)gameObject).isDead()) {
                        toRemove.add((Troop) gameObject);
                        continue;
                    }
                }

                gameObject.update();
            }

            gameObjects.removeAll(toRemove);

            List<ScheduledAction> actionsThisTick = actionQueue.get(this.tick);
            if (actionsThisTick != null) {
                for (ScheduledAction action : actionsThisTick) {
                    action.execute(this); 
                }
            }

            actionQueue.remove(this.tick);

            long currChecksum = updateChecksum();
            this.currentChecksum = currChecksum;
            if(this.isServer) {
                checksumHistory[this.tick % HISTORY_SIZE] = currChecksum;
            }
        }
    }

    public void scheduleFromNetwork(Runnable task) {
        taskQueue.offer(task);
    }

    private void processNetworkTasks() {
        Runnable task;
        while ((task = taskQueue.poll()) != null) {
            task.run(); 
        }
    }

    public void spawnObject(int type, int team, int lane) {
        int requiredGold = 0;
        if (type == 0) {
            requiredGold = 75;
        } else if (type == 1) {
            requiredGold = 250;
        } else if (type == 2) {
            requiredGold = 50;
        } else if (type == 3) {
            requiredGold = 150;
        }

        GameObject newObj = null;
        
        int y;
        if (lane == 1) {
            y = 420;
        } else if (lane == 2) {
            y = 300;
        } else {
            y = 180;
        } 
        int x;
        if(team == 0) {
            x = 512;
        } else {
            x = 1410; 
        }

        if (players[team].getGold() > requiredGold) {
            switch(type) {
            case 0:
                newObj = new Archer(x, y, team);
                break;
            case 1:
                newObj = new Dragon(x, y , team); 
                break;
            case 2:
                newObj = new Knight(x, y, team);
                break;
            case 3: 
                newObj = new Mage(x, y, team);
                break;
            default:
                System.out.println("unknown object");
                break;
            }

            newObj.setId(currentObjId);

            incrementObjId();

            if(newObj != null) {
                gameObjects.add(newObj);
            }
        } 
    }

    public void addObject(GameObject object) {
        gameObjects.add(object);
    }

    //used by client
    public void addAction(ScheduledAction action, int tick) {
        actionQueue.putIfAbsent(tick, new ArrayList<>());
        actionQueue.get(tick).add(action);
    }

    public ScheduledAction scheduleAction(ScheduledAction action) {
        int scheduledTick = this.tick + 10;
        action.setTick(scheduledTick);
        addAction(action, scheduledTick);

        return action;
    }

    public void correct(Snapshot snapshot) {
        int tickDiff = this.tick - snapshot.getTick();

        this.actionQueue.clear();
        for (ScheduledAction action : snapshot.getActions()) {
            this.addAction(action, action.getTick());
        }

        this.gameObjects.clear();
        for (GameObject serverObj : snapshot.getObjects()) {
            this.addObject(serverObj);
        }

        this.towerList[0] = snapshot.towers[0];
        this.towerList[1] = snapshot.towers[1];

        this.tick = snapshot.getTick();
        this.currentObjId = snapshot.nextObjectId;

        if(tickDiff > 0) {
            for(int i = 0; i < tickDiff; i++) {
                this.update();
            }
        }
    }

    private long updateChecksum() {
        crc32.reset();
        
        gameObjects.sort((a, b) -> Integer.compare(a.getId(), b.getId()));

        for (GameObject obj : gameObjects) {
            ((java.nio.Buffer) buffer).clear();
            buffer.putInt(obj.getId());
            buffer.putInt(obj.getX());
            buffer.putInt(obj.getY());
            crc32.update(buffer.array(), 0, 12);
        }
        return crc32.getValue();
    }

    public void incrementObjId() {
        this.currentObjId++;
    }

    public Snapshot getSnapshot() {
        return new Snapshot(this.tick, this.currentObjId, gameObjects, actionQueue);
    }

    public static class Snapshot {
        private int tick;
        private int nextObjectId;
        private List<GameObject> objects;
        private List<ScheduledAction> actions;
        private Tower[] towers = new Tower[2];

        public Snapshot(int tick, int nextObjectId, List<GameObject> serverObjects, Map<Integer, List<ScheduledAction>> serverActionsMap) {
            this.tick = tick;
            this.nextObjectId = nextObjectId;
            this.objects = serverObjects;
            
            this.actions = new ArrayList<>();

            for (List<ScheduledAction> actionList : serverActionsMap.values()) {
                this.actions.addAll(actionList);
            }
        }

        public Snapshot(ByteBuf buf) {
            this.tick = buf.readInt();
            this.nextObjectId = buf.readInt();

            this.objects = new ArrayList<>();
            this.actions = new ArrayList<>();

            //read objs
            int objCount = buf.readInt();

            for(int i = 0; i < objCount; i++) {
                GameObject ob = ObjectDecoder.decode(buf);

                if(ob.getType() == Troop.TOWER) {
                    towers[ob.getTeam()] = (Tower) ob;
                }

                objects.add(ob);
            }

            //read actions
            int actionCount = buf.readInt();

            for(int i = 0; i < actionCount; i++) {
                actions.add(ActionDecoder.decode(buf)); 
            }
        }

        public void write(ByteBuf buf) {
            buf.writeInt(tick);
            buf.writeInt(nextObjectId);

            //objects
            buf.writeInt(objects.size());

            for(GameObject obj : objects) {
                ObjectEncoder.encode(buf, obj);
            }

            //actions
            buf.writeInt(actions.size());

            for(ScheduledAction action : actions) {
                ActionEncoder.encode(buf, action);
            }
        }

        public int getTick() {return tick;}
        public int getNextObjectId() {return nextObjectId;}
        public Tower[] getTowers() {return towers;}
        public List<GameObject> getObjects() {return objects;}
        public List<ScheduledAction> getActions() {return actions;}
    }

    public long getCurrentChecksum() {
        return this.currentChecksum;
    }

    public long getChecksum(int tick) {
        return checksumHistory[tick % HISTORY_SIZE];
    }

    public int getTick() {
        return tick;
    }

    public SimPlayer getSimPlayer(int id) {
        return players[id];
    }
}
