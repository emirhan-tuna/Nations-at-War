package io.github.some_example_name;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TroopManager {
    
    private List<Troop> p1Troops, p2Troops;

    public TroopManager(){
        this.p1Troops = new ArrayList<>();
        this.p2Troops = new ArrayList<>();
    }

    public void updateAll(float delta){
        for(Troop t: p1Troops){
            t.update(delta);
        }
        for(Troop t: p2Troops){
            t.update(delta);
        }
        resolveCollisions(); // checks for combat between opposing troops
        removeDead(p1Troops);
        removeDead(p2Troops);
    }

    public void resolveCollisions(){
        for(Troop attacker: p1Troops){
            findAndEngageTarget(attacker, p2Troops);
        }
        for(Troop attacker: p2Troops){
            findAndEngageTarget(attacker, p1Troops);
        }
    }

    // Finds the closest enemy in range for the attacker. If found, sets it as target and attacks.
    private void findAndEngageTarget(Troop attacker, List<Troop> enemies){
        Troop closest = null;
        float closestDist = Float.MAX_VALUE;
        for(Troop enemy: enemies){
            if(enemy.isDead()) continue;

            float dx = enemy.getX() - attacker.getX();
            float dy = enemy.getY() - attacker.getY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if(dist < closestDist){
                closestDist = dist;
                closest = enemy;
            }
        }
        if(closest != null && closestDist <= attacker.getRange()){
            attacker.setTarget(closest);
            attacker.attack(closest);
        }
        else if(closest != null){
            attacker.setTarget(closest);
            attacker.moveTo(closest.getX(), closest.getY());
        }
    }

    public void spawn(String type, int ownerID, float x, float y){
        Troop troop = createTroop(type, x, y, ownerID);

        if(troop == null){
            System.out.println("Unknown troop type: " + type);
            return;
        }
        if(ownerID == 1){
            p1Troops.add(troop);
        }
        else{
            p2Troops.add(troop);
        }
    }

    private Troop createTroop(String type, float x, float y, int ownerID){
        switch(type.toLowerCase()){
            // case "swordsman": return new Swordsman(x, y, ownerID);
            // case "knight":    return new Knight(x, y, ownerID);
            // case "archer":    return new Archer(x, y, ownerID);
            // case "mage":      return new Mage(x, y, ownerID);
            // case "dragon":    return new Dragon(x, y, ownerID);
            default:
                return null;
        }
    }

    public void removeDead(List<Troop> troops){
        Iterator<Troop> it = troops.iterator();
        while(it.hasNext()){
            if(it.next().isDead()){
                it.remove();
            }
        }
    }

    // getters
    public List<Troop> getP1Troops(){ return p1Troops; }
    public List<Troop> getP2Troops(){ return p2Troops; }
}
