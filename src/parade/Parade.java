/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parade;

import iat265.aga53.CreatureFactory;
import iat265.aga53.Scrubbable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import processing.core.PApplet;

/**
 *
 * @author aga53
 */
public class Parade extends PApplet {

    List<Scrubbable> scrubbables = null;
    Scrubbable current = null;
    long lastUpdate;
    int index = 0;

    public void settings() {
        fullScreen();
    }

    public void setup() {
        scrubbables = getAnimals();

        System.out.println(scrubbables.size() + " animals found.");
        if (scrubbables.size() > 0) {
            current = scrubbables.get(0);
        }
        lastUpdate = millis();
    }

    public void draw() {
        background(255);
        if (scrubbables.size() > 0 && (millis() - lastUpdate > 2000)) {
            index = (index + 1) % scrubbables.size();
            current = scrubbables.get(index);
            lastUpdate = millis();
        }
        if (current != null) {
            try {

                for (Iterator<Scrubbable> iterator = current.createIterator(); iterator.hasNext();) {

                    iterator.next().draw();

                }
            } catch (Exception e) {

            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PApplet.main(Parade.class.getCanonicalName());
    }

    private List<Scrubbable> getAnimals() {
        List<Scrubbable> creatures = new ArrayList<>(50);
        Reflections conf = new Reflections("iat265");
        Set<Class<? extends CreatureFactory>> factories = conf.getSubTypesOf(CreatureFactory.class);
        for (Iterator<Class<? extends CreatureFactory>> iterator = factories.iterator(); iterator.hasNext();) {
            Class<? extends CreatureFactory> next = iterator.next();
            System.out.println(next.getCanonicalName());
        }
        for (Class<? extends CreatureFactory> k : factories) {
            Scrubbable creature = null;
            try {
                CreatureFactory factory = k.newInstance();
                creature = factory.getCreature(this);
                if (creature == null) {
                    System.err.println("Your creature factory getCreature() method is returning a null value");
                } else {
                    creatures.add(creature);
                }
            } catch (InstantiationException ex) {
                System.err.println(k.getCanonicalName() + ":Please make sure that your factory class does not have a constructor with 1 or more arguments.");
            } catch (IllegalAccessException ex) {
                System.err.println(k.getCanonicalName() + ":Please make sure that the factory constructor (if you have one) is public.");
            }
        }
        return creatures;
    }
}
