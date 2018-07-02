/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.jnp.grapes.services;

import org.jlab.jnp.hipo.data.HipoEvent;
import org.jlab.jnp.hipo.data.HipoNode;
import org.jlab.jnp.physics.EventFilter;
import org.jlab.jnp.physics.Particle;
import org.jlab.jnp.physics.ParticleList;
import org.jlab.jnp.utils.json.Json;
import org.jlab.jnp.utils.json.JsonObject;

/**
 *
 * @author gavalian
 */
public class GenericWagon extends Wagon {
    
    private EventFilter eventFilter = new EventFilter();
    
    public GenericWagon() {
        super("GenericWagon", "gavalian", "1.0");
    }

    @Override
    public boolean processDataEvent(HipoEvent event) {
        
        HipoNode node = event.getNode(331, 1);
        ParticleList list = new ParticleList();        
        
        if(node.getDataSize()>0){
            for(int i = 0; i < node.getDataSize(); i++){
                int pid = node.getInt(i);
                Particle p = new Particle();
                if(pid!=0){
                    p.initParticle(pid, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
                } else {
                    p.initParticleWithPidMassSquare(pid, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
                }
                list.add(p);
            }
            return eventFilter.checkFinalState(list);
        }
        return false;
    }

    @Override
    public boolean init(String jsonString) {
        JsonObject jsonObj = Json.parse(jsonString).asObject();
        String filterString = jsonObj.getString("filter","X+:X-:Xn");
        this.eventFilter.setFilter(filterString);
        System.out.println(" WAGON CONFIGURATION : set filter = " + filterString);
        return true;
    }
    
}
