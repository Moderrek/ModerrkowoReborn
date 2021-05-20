package pl.moderr.moderrkowo.reborn.timevoter;

import pl.moderr.moderrkowo.reborn.Main;
import pl.moderr.moderrkowo.reborn.timevoter.utils.VoteCommand;
import pl.moderr.moderrkowo.reborn.timevoter.utils.VoteEvent;
import pl.moderr.moderrkowo.reborn.timevoter.utils.VoteUtil;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class TimeVoter {

    private final Set<UUID> noVote = new HashSet<>();
    private final Set<UUID> yesVote = new HashSet<>();

    public int timeToVote;
    public boolean isVoteActive;
    public int voteDelay;
    public Instant lastVote;
    public VoteUtil voteUtil;

    public TimeVoter() {
        timeToVote = Main.getInstance().getConfig().getInt("time-to-vote");
        voteDelay = Main.getInstance().getConfig().getInt("vote-delay");
        voteUtil = new VoteUtil(this);
        Main.getInstance().getServer().getPluginManager().registerEvents(new VoteEvent(this), Main.getInstance());
        Objects.requireNonNull(Main.getInstance().getCommand("timevote")).setExecutor(new VoteCommand(this));
    }

    public void Disable() {
        yesVote.clear();
        noVote.clear();
    }

    public Set<UUID> getNoVote() {
        return noVote;
    }

    public Set<UUID> getYesVote() {
        return yesVote;
    }
}
