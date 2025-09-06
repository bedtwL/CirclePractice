package me.itsglobally.circlePractice.bb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class b1 {

    private final UUID id;
    private final b4 player1;
    private final b4 player2;
    private final String kit;
    private final b0 b0;
    private final long startTime;
    private final List<UUID> spectators;
    private DuelState state;
    private int countdown;

    public b1(b4 player1, b4 player2, String kit, b0 b0) {
        this.id = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
        this.kit = kit;
        this.b0 = b0;
        this.startTime = System.currentTimeMillis();
        this.spectators = new ArrayList<>();
        this.state = DuelState.STARTING;
        this.countdown = 5;
    }

    public UUID getId() {
        return id;
    }

    public b4 getPlayer1() {
        return player1;
    }

    public b4 getPlayer2() {
        return player2;
    }

    public String getKit() {
        return kit;
    }

    public b0 getArena() {
        return b0;
    }

    public long getStartTime() {
        return startTime;
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public DuelState getState() {
        return state;
    }

    public void setState(DuelState state) {
        this.state = state;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public b4 getOpponent(b4 player) {
        return player.equals(player1) ? player2 : player1;
    }

    public boolean containsPlayer(UUID uuid) {
        return player1.getUuid().equals(uuid) || player2.getUuid().equals(uuid);
    }

    public void addSpectator(UUID uuid) {
        spectators.add(uuid);
    }

    public void removeSpectator(UUID uuid) {
        spectators.remove(uuid);
    }

    public enum DuelState {
        STARTING, ACTIVE, FINISHED
    }
}