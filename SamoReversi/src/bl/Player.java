package bl;

public class Player {

	private String name = "player";
	private int score = 0;
	
	public Player() {
	}

	public Player(String playerName){
		this();
		setName(playerName);		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
