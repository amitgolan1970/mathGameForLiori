package com.golan.amit.puzekmathgame.helper;

import java.util.Random;

public class MathGameHelper {
    public static final int ROUNDS = 10;
    public static final int HINTS = 3;
    public static final int ATTEMPTS = 3;
    public static final int SCORE_INCREASE_FACTOR = 10;
    public static final int SCORE_DECREASE_FACTOR = 3;
    public static final int SCORE_DECREASE_FACTOR_FAIL = 5;

    /**
     * Math operands (one % two)
     */
    private int one;
    private int two;

    /**
     * stats
     */
    private int rounds;
    private int attempts;
    private int total_attempts;
    private int successes;
    private int fail_count;
    private int hints;

    /**
     * Scores
     */
    private int score;
    private int current_high_score;

    /**
     * Methods
     */

    public void generateRandomValues() {
        Random rnd = new Random();
        one = rnd.nextInt(90) + 10;
        two = rnd.nextInt(10) + 2;
    }

    public int getOne() {
        return this.one;
    }

    public int getTwo() {
        return this.two;
    }

    public int getModRes() {
        return one % two;
    }

    public int getDivRes() {
        return one / two;
    }

    public int getRounds() {
        return rounds;
    }

    public void increaseRounds() {
        this.rounds++;
    }

    public void resetRounds() {
        this.rounds = 1;
    }

    public void increaseSuccesses() {
        this.successes++;
    }

    public void resetSuccesses() {
        this.successes = 0;
    }

    public int getSuccesses() {
        return this.successes;
    }

    public int getHints() {
        return this.hints;
    }

    public void increaseHints() {
        this.hints++;
    }

    public void resetHints() {
        this.hints = 0;
    }

    /**
     * On fails handling
     */
    public void increaseAttempts() {
        this.attempts++;
    }

    public void resetAttempts() {
        this.attempts = 0;
    }

    public int getAttempts() {
        return this.attempts;
    }

    public void increaseFailCount() { this.fail_count++; }

    public void resetFailCount() { this.fail_count = 0; }

    public int getFailCount() { return this.fail_count; }

    public void increaseTotalAttempts() { this.total_attempts++; }

    public void resetTotalAttempts() { this.total_attempts = 0; }

    public int getTotalAttempts() { return this.total_attempts; }

    public void addToScore(int aScore) { this.score += aScore; }

    public void subtractFromScore(int aScore) { this.score -= aScore; }

    public int getScore() { return this.score; }

    public int getCurrent_high_score() {
        return current_high_score;
    }

    public void setCurrent_high_score(int current_high_score) {
        this.current_high_score = current_high_score;
    }
}
