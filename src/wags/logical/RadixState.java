package wags.logical;

public class RadixState { 
	
	// State for radix positions and dequeues 
	public static enum State {
		Ones, DOnes, Tens, DTens, Hundreds, DHundreds, End
	}
	
	public boolean complete;
	private State state;
	
	
	public RadixState() {
		state = State.Ones;
		complete = false;
	}
	
	public boolean advance(boolean sorted) {
		if (sorted && !complete) {
			state = State.values()[state.ordinal() + 1];
		}
		if (state == State.End) {
			complete = true;
		}
		return sorted;
	}
	
	public State getCurrentState() {
		return state;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
}
