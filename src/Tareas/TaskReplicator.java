package Tareas;

import puertoscafe.Slot;

public class TaskReplicator {
    Slot s;
    public TaskReplicator(Slot Bebidas) {
      this.s=Bebidas;
    }
    public Slot replicate(){
        return this.s;
    }
    
}
