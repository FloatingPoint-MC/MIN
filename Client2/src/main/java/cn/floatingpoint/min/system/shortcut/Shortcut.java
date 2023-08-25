package cn.floatingpoint.min.system.shortcut;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-25 19:07:26
 */
public record Shortcut(int key, Action action) {
    public record Action(Type type, String context) {
        public enum Type {
            RUN_COMMAND,
            EXIT_GAME
        }
    }
}
