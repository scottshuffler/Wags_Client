package wags;

import wags.logical.Evaluation;

public class LogicalProblem{
	
	public int id;
	public String title, directions, type, genre;
	
	public LogicalProblem(int id, String title, String directions, String type, String genre) {
		this.id = id;
		this.title = title;
		this.directions = directions;
		this.type = type;
		this.genre = genre;
	}

}
