package edu.ntua.dblab.hecataeus.graph.evolution.messages;
import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionEdge;
import edu.ntua.dblab.hecataeus.graph.evolution.EvolutionNode;
import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public class ModuleMaestro<V extends EvolutionNode<E>,E extends EvolutionEdge>
{
	PriorityQueue<Message<V,E>> forGlobal;
	public PriorityQueue<Message<V,E>> myQueue;
	PriorityQueue<Message<V,E>> global;
	public Message<V,E> arxikoMinima;

	public ModuleMaestro(PriorityQueue<Message<V,E>> globalQueue)
	{
		global=globalQueue;
		arxikoMinima=globalQueue.peek();
		forGlobal=new PriorityQueue<Message<V,E>>(1, new MessageCompare());
		myQueue=new PriorityQueue<Message<V,E>>(1, new MessageCompare());
		while(globalQueue.peek()!=null&&globalQueue.peek().toNode==arxikoMinima.toNode)
		{
			myQueue.add(globalQueue.poll());
		}
	}
	
	public void propagateMessages()
	{
		while(myQueue.peek()!=null)
		{
			Message<V,E> currentMessage=myQueue.poll();
			MaestroAbstract<V,E> maestro = MaestroFactory.create(currentMessage,forGlobal);
			maestro.propagateMessage(currentMessage);
		}
		if(arxikoMinima.toNode.getStatus()==StatusType.PROPAGATE)
		{
			global.addAll(forGlobal);
		}
	}
}
