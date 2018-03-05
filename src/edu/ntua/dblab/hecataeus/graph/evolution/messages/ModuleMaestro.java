package edu.ntua.dblab.hecataeus.graph.evolution.messages;

import java.util.PriorityQueue;

import edu.ntua.dblab.hecataeus.graph.evolution.StatusType;

public class ModuleMaestro {
	PriorityQueue<Message> forGlobal;
	public PriorityQueue<Message> myQueue;
	PriorityQueue<Message> global;
	public Message arxikoMinima;

	public ModuleMaestro(final PriorityQueue<Message> globalQueue) {
		global = globalQueue;
		arxikoMinima = globalQueue.peek();
		forGlobal = new PriorityQueue<>(1, new MessageCompare());
		myQueue = new PriorityQueue<>(1, new MessageCompare());
		while (globalQueue.peek() != null && globalQueue.peek().toNode == arxikoMinima.toNode)
			myQueue.add(globalQueue.poll());
	}

	public void propagateMessages() {
		while (myQueue.peek() != null) {
			final Message currentMessage = myQueue.poll();
			final MaestroAbstract maestro = MaestroFactory.create(currentMessage, forGlobal);
			maestro.propagateMessage(currentMessage);
		}
		if (arxikoMinima.toNode.getStatus() == StatusType.PROPAGATE)
			global.addAll(forGlobal);
	}
}
