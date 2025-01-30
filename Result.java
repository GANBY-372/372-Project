import java.io.Serializable;

public class Result implements Serializable {
    private String TeamName;
    private long goals;

    public Result(String teamName, long goals) {
        this.TeamName = teamName;
        this.goals = goals;
    }

    public String getTeamName() {
        return TeamName;
    }
    public long getGoals() {
        return goals;
    }

}
