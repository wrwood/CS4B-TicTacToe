package Game.Util;

public class Config {
    // Default values
    public static final String DEFAULT_PLAYER1_MARKER = "x";
    public static final String DEFAULT_PLAYER2_MARKER = "o";
    public static final GameModes DEFAULT_GAME_MODE = GameModes.SINGLE_PLAYER;

    // Event Types
    public static final String PLAYER_TURN = "PlayerTurn";
    public static final String PLAYER2_MOVE = "Player2Move";
    public static final String GAME_OVER = "GameOver";

    // Image paths
    public static final String PLAYER1_MARKER_IMAGE = "/images/dragon_egg_png_overlay__by_lewis4721_de8r1hj-fullview.png";
    public static final String PLAYER2_MARKER_IMAGE = "/images/dragon_egg_png_overlay__by_lewis4721_de8r1hq-414w-2x.png";
}
