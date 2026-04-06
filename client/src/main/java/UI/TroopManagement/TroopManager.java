package UI.TroopManagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Game.Main;

public class TroopManager {
    
    public ArrayList<Troop> p1Troops, p2Troops;
    public Main game;
    private Texture swordsman, archer, dragon, knight, mage;

    public TroopManager(){
        this.p1Troops = new ArrayList<>();
        this.p2Troops = new ArrayList<>();

        archer = new Texture(Gdx.files.internal("sprites/archer_sprite.png"));
        dragon = new Texture(Gdx.files.internal("sprites/dragon_sprite.png"));
        knight = new Texture(Gdx.files.internal("sprites/knight_sprite.png"));
        mage = new Texture(Gdx.files.internal("sprites/mage_sprite.png"));
    }

    public void updateAll(float delta){
        for(Troop t: p1Troops){
            t.update(delta);
        }
        for(Troop t: p2Troops){
            t.update(delta);
        }
        resolveCollisions(delta);
        removeDead(p1Troops);
        removeDead(p2Troops);
    }

    public void resolveCollisions(float delta){
        for(Troop attacker: p1Troops){
            findAndEngageTarget(attacker, p2Troops, delta);
        }
        for(Troop attacker: p2Troops){
            findAndEngageTarget(attacker, p1Troops, delta);
        }
    }

    // Finds the closest enemy in range for the attacker. If found, sets it as target and attacks.
    private void findAndEngageTarget(Troop attacker, List<Troop> enemies, float delta) {
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
            attacker.moveTo(closest.getX(), closest.getY(), delta);
        } else {
            if (attacker.getOwnerID() == 1) {
                attacker.moveTo(1920f, attacker.getY(), delta);
            } else {
                attacker.moveTo(0f, attacker.getY(), delta);
            }
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

    public Troop createTroop(String type, float x, float y, int ownerID){
        switch(type.toLowerCase()){
            case "swordsman": return new Swordsman(swordsman, x, y, ownerID);
            case "knight": return new Knight(knight, x, y, ownerID);
            case "archer": return new Archer(archer, x, y, ownerID, 150);
            case "mage": return new Mage(mage, x, y, ownerID, 120, 60);
            case "dragon": return new Dragon(dragon, x, y, ownerID);
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

    public void drawAll(SpriteBatch batch) {
        for (int i = 0; i < p1Troops.size(); i++) {
            p1Troops.get(i).draw(batch);
        }
        for (int i = 0; i < p2Troops.size(); i++) {
            p2Troops.get(i).draw(batch);
        }
    }

    // getters
    public ArrayList<Troop> getP1Troops(){ return p1Troops; }
    public ArrayList<Troop> getP2Troops(){ return p2Troops; }
}
