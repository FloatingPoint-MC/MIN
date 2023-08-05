package org.lwjglx.input;

import java.awt.event.KeyEvent;
import org.lwjgl.glfw.GLFW;

public class KeyCodes {
    public static int glfwToLwjgl(int glfwKeyCode) {
        if (glfwKeyCode > GLFW.GLFW_KEY_LAST) {
            return glfwKeyCode;
        }
        
        int keyCode;
        switch (glfwKeyCode) {
            // arbitrary mapping to fix text input here
            case GLFW.GLFW_KEY_UNKNOWN: {
                keyCode = Keyboard.KEY_UNLABELED;
                break;
            }
            case GLFW.GLFW_KEY_ESCAPE: {
                keyCode = Keyboard.KEY_ESCAPE;
                break;
            }
            case GLFW.GLFW_KEY_BACKSPACE: {
                keyCode = Keyboard.KEY_BACK;
                break;
            }
            case GLFW.GLFW_KEY_TAB: {
                keyCode = Keyboard.KEY_TAB;
                break;
            }
            case GLFW.GLFW_KEY_ENTER: {
                keyCode = Keyboard.KEY_RETURN;
                break;
            }
            case GLFW.GLFW_KEY_SPACE: {
                keyCode = Keyboard.KEY_SPACE;
                break;
            }
            case GLFW.GLFW_KEY_LEFT_CONTROL: {
                keyCode = Keyboard.KEY_LCONTROL;
                break;
            }
            case GLFW.GLFW_KEY_LEFT_SHIFT: {
                keyCode = Keyboard.KEY_LSHIFT;
                break;
            }
            case GLFW.GLFW_KEY_LEFT_ALT: {
                keyCode = Keyboard.KEY_LMENU;
                break;
            }
            case GLFW.GLFW_KEY_LEFT_SUPER: {
                keyCode = Keyboard.KEY_LMETA;
                break;
            }
            case GLFW.GLFW_KEY_RIGHT_CONTROL: {
                keyCode = Keyboard.KEY_RCONTROL;
                break;
            }
            case GLFW.GLFW_KEY_RIGHT_SHIFT: {
                keyCode = Keyboard.KEY_RSHIFT;
                break;
            }
            case GLFW.GLFW_KEY_RIGHT_ALT: {
                keyCode = Keyboard.KEY_RMENU;
                break;
            }
            case GLFW.GLFW_KEY_RIGHT_SUPER: {
                keyCode = Keyboard.KEY_RMETA;
                break;
            }
            case GLFW.GLFW_KEY_1: {
                keyCode = Keyboard.KEY_1;
                break;
            }
            case GLFW.GLFW_KEY_2: {
                keyCode = Keyboard.KEY_2;
                break;
            }
            case GLFW.GLFW_KEY_3: {
                keyCode = Keyboard.KEY_3;
                break;
            }
            case GLFW.GLFW_KEY_4: {
                keyCode = Keyboard.KEY_4;
                break;
            }
            case GLFW.GLFW_KEY_5: {
                keyCode = Keyboard.KEY_5;
                break;
            }
            case GLFW.GLFW_KEY_6: {
                keyCode = Keyboard.KEY_6;
                break;
            }
            case GLFW.GLFW_KEY_7: {
                keyCode = Keyboard.KEY_7;
                break;
            }
            case GLFW.GLFW_KEY_8: {
                keyCode = Keyboard.KEY_8;
                break;
            }
            case GLFW.GLFW_KEY_9: {
                keyCode = Keyboard.KEY_9;
                break;
            }
            case GLFW.GLFW_KEY_0: {
                keyCode = Keyboard.KEY_0;
                break;
            }
            case GLFW.GLFW_KEY_A: {
                keyCode = Keyboard.KEY_A;
                break;
            }
            case GLFW.GLFW_KEY_B: {
                keyCode = Keyboard.KEY_B;
                break;
            }
            case GLFW.GLFW_KEY_C: {
                keyCode = Keyboard.KEY_C;
                break;
            }
            case GLFW.GLFW_KEY_D: {
                keyCode = Keyboard.KEY_D;
                break;
            }
            case GLFW.GLFW_KEY_E: {
                keyCode = Keyboard.KEY_E;
                break;
            }
            case GLFW.GLFW_KEY_F: {
                keyCode = Keyboard.KEY_F;
                break;
            }
            case GLFW.GLFW_KEY_G: {
                keyCode = Keyboard.KEY_G;
                break;
            }
            case GLFW.GLFW_KEY_H: {
                keyCode = Keyboard.KEY_H;
                break;
            }
            case GLFW.GLFW_KEY_I: {
                keyCode = Keyboard.KEY_I;
                break;
            }
            case GLFW.GLFW_KEY_J: {
                keyCode = Keyboard.KEY_J;
                break;
            }
            case GLFW.GLFW_KEY_K: {
                keyCode = Keyboard.KEY_K;
                break;
            }
            case GLFW.GLFW_KEY_L: {
                keyCode = Keyboard.KEY_L;
                break;
            }
            case GLFW.GLFW_KEY_M: {
                keyCode = Keyboard.KEY_M;
                break;
            }
            case GLFW.GLFW_KEY_N: {
                keyCode = Keyboard.KEY_N;
                break;
            }
            case GLFW.GLFW_KEY_O: {
                keyCode = Keyboard.KEY_O;
                break;
            }
            case GLFW.GLFW_KEY_P: {
                keyCode = Keyboard.KEY_P;
                break;
            }
            case GLFW.GLFW_KEY_Q: {
                keyCode = Keyboard.KEY_Q;
                break;
            }
            case GLFW.GLFW_KEY_R: {
                keyCode = Keyboard.KEY_R;
                break;
            }
            case GLFW.GLFW_KEY_S: {
                keyCode = Keyboard.KEY_S;
                break;
            }
            case GLFW.GLFW_KEY_T: {
                keyCode = Keyboard.KEY_T;
                break;
            }
            case GLFW.GLFW_KEY_U: {
                keyCode = Keyboard.KEY_U;
                break;
            }
            case GLFW.GLFW_KEY_V: {
                keyCode = Keyboard.KEY_V;
                break;
            }
            case GLFW.GLFW_KEY_W: {
                keyCode = Keyboard.KEY_W;
                break;
            }
            case GLFW.GLFW_KEY_X: {
                keyCode = Keyboard.KEY_X;
                break;
            }
            case GLFW.GLFW_KEY_Y: {
                keyCode = Keyboard.KEY_Y;
                break;
            }
            case GLFW.GLFW_KEY_Z: {
                keyCode = Keyboard.KEY_Z;
                break;
            }
            case GLFW.GLFW_KEY_UP: {
                keyCode = Keyboard.KEY_UP;
                break;
            }
            case GLFW.GLFW_KEY_DOWN: {
                keyCode = Keyboard.KEY_DOWN;
                break;
            }
            case GLFW.GLFW_KEY_LEFT: {
                keyCode = Keyboard.KEY_LEFT;
                break;
            }
            case GLFW.GLFW_KEY_RIGHT: {
                keyCode = Keyboard.KEY_RIGHT;
                break;
            }
            case GLFW.GLFW_KEY_INSERT: {
                keyCode = Keyboard.KEY_INSERT;
                break;
            }
            case GLFW.GLFW_KEY_DELETE: {
                keyCode = Keyboard.KEY_DELETE;
                break;
            }
            case GLFW.GLFW_KEY_HOME: {
                keyCode = Keyboard.KEY_HOME;
                break;
            }
            case GLFW.GLFW_KEY_END: {
                keyCode = Keyboard.KEY_END;
                break;
            }
            case GLFW.GLFW_KEY_PAGE_UP: {
                keyCode = Keyboard.KEY_PRIOR;
                break;
            }
            case GLFW.GLFW_KEY_PAGE_DOWN: {
                keyCode = Keyboard.KEY_NEXT;
                break;
            }
            case GLFW.GLFW_KEY_F1: {
                keyCode = Keyboard.KEY_F1;
                break;
            }
            case GLFW.GLFW_KEY_F2: {
                keyCode = Keyboard.KEY_F2;
                break;
            }
            case GLFW.GLFW_KEY_F3: {
                keyCode = Keyboard.KEY_F3;
                break;
            }
            case GLFW.GLFW_KEY_F4: {
                keyCode = Keyboard.KEY_F4;
                break;
            }
            case GLFW.GLFW_KEY_F5: {
                keyCode = Keyboard.KEY_F5;
                break;
            }
            case GLFW.GLFW_KEY_F6: {
                keyCode = Keyboard.KEY_F6;
                break;
            }
            case GLFW.GLFW_KEY_F7: {
                keyCode = Keyboard.KEY_F7;
                break;
            }
            case GLFW.GLFW_KEY_F8: {
                keyCode = Keyboard.KEY_F8;
                break;
            }
            case GLFW.GLFW_KEY_F9: {
                keyCode = Keyboard.KEY_F9;
                break;
            }
            case GLFW.GLFW_KEY_F10: {
                keyCode = Keyboard.KEY_F10;
                break;
            }
            case GLFW.GLFW_KEY_F11: {
                keyCode = Keyboard.KEY_F11;
                break;
            }
            case GLFW.GLFW_KEY_F12: {
                keyCode = Keyboard.KEY_F12;
                break;
            }
            case GLFW.GLFW_KEY_F13: {
                keyCode = Keyboard.KEY_F13;
                break;
            }
            case GLFW.GLFW_KEY_F14: {
                keyCode = Keyboard.KEY_F14;
                break;
            }
            case GLFW.GLFW_KEY_F15: {
                keyCode = Keyboard.KEY_F15;
                break;
            }
            case GLFW.GLFW_KEY_F16: {
                keyCode = Keyboard.KEY_F16;
                break;
            }
            case GLFW.GLFW_KEY_F17: {
                keyCode = Keyboard.KEY_F17;
                break;
            }
            case GLFW.GLFW_KEY_F18: {
                keyCode = Keyboard.KEY_F18;
                break;
            }
            case GLFW.GLFW_KEY_F19: {
                keyCode = Keyboard.KEY_F19;
                break;
            }
            case GLFW.GLFW_KEY_KP_1: {
                keyCode = Keyboard.KEY_NUMPAD1;
                break;
            }
            case GLFW.GLFW_KEY_KP_2: {
                keyCode = Keyboard.KEY_NUMPAD2;
                break;
            }
            case GLFW.GLFW_KEY_KP_3: {
                keyCode = Keyboard.KEY_NUMPAD3;
                break;
            }
            case GLFW.GLFW_KEY_KP_4: {
                keyCode = Keyboard.KEY_NUMPAD4;
                break;
            }
            case GLFW.GLFW_KEY_KP_5: {
                keyCode = Keyboard.KEY_NUMPAD5;
                break;
            }
            case GLFW.GLFW_KEY_KP_6: {
                keyCode = Keyboard.KEY_NUMPAD6;
                break;
            }
            case GLFW.GLFW_KEY_KP_7: {
                keyCode = Keyboard.KEY_NUMPAD7;
                break;
            }
            case GLFW.GLFW_KEY_KP_8: {
                keyCode = Keyboard.KEY_NUMPAD8;
                break;
            }
            case GLFW.GLFW_KEY_KP_9: {
                keyCode = Keyboard.KEY_NUMPAD9;
                break;
            }
            case GLFW.GLFW_KEY_KP_0: {
                keyCode = Keyboard.KEY_NUMPAD0;
                break;
            }
            case GLFW.GLFW_KEY_KP_ADD: {
                keyCode = Keyboard.KEY_ADD;
                break;
            }
            case GLFW.GLFW_KEY_KP_SUBTRACT: {
                keyCode = Keyboard.KEY_SUBTRACT;
                break;
            }
            case GLFW.GLFW_KEY_KP_MULTIPLY: {
                keyCode = Keyboard.KEY_MULTIPLY;
                break;
            }
            case GLFW.GLFW_KEY_KP_DIVIDE: {
                keyCode = Keyboard.KEY_DIVIDE;
                break;
            }
            case GLFW.GLFW_KEY_KP_DECIMAL: {
                keyCode = Keyboard.KEY_DECIMAL;
                break;
            }
            case GLFW.GLFW_KEY_KP_EQUAL: {
                keyCode = Keyboard.KEY_NUMPADEQUALS;
                break;
            }
            case GLFW.GLFW_KEY_KP_ENTER: {
                keyCode = Keyboard.KEY_NUMPADENTER;
                break;
            }
            case GLFW.GLFW_KEY_NUM_LOCK: {
                keyCode = Keyboard.KEY_NUMLOCK;
                break;
            }
            case GLFW.GLFW_KEY_SEMICOLON: {
                keyCode = Keyboard.KEY_SEMICOLON;
                break;
            }
            case GLFW.GLFW_KEY_BACKSLASH: {
                keyCode = Keyboard.KEY_BACKSLASH;
                break;
            }
            case GLFW.GLFW_KEY_COMMA: {
                keyCode = Keyboard.KEY_COMMA;
                break;
            }
            case GLFW.GLFW_KEY_PERIOD: {
                keyCode = Keyboard.KEY_PERIOD;
                break;
            }
            case GLFW.GLFW_KEY_SLASH: {
                keyCode = Keyboard.KEY_SLASH;
                break;
            }
            case GLFW.GLFW_KEY_GRAVE_ACCENT: {
                keyCode = Keyboard.KEY_GRAVE;
                break;
            }
            case GLFW.GLFW_KEY_CAPS_LOCK: {
                keyCode = Keyboard.KEY_CAPITAL;
                break;
            }
            case GLFW.GLFW_KEY_SCROLL_LOCK: {
                keyCode = Keyboard.KEY_SCROLL;
                break;
            }
            // "World" keys could be anything depending on
            // keyboard layout, pick something arbitrary
            case GLFW.GLFW_KEY_WORLD_1: {
                keyCode = Keyboard.KEY_CIRCUMFLEX;
                break;
            }
            case GLFW.GLFW_KEY_WORLD_2: {
                keyCode = Keyboard.KEY_YEN;
                break;
            }
            case GLFW.GLFW_KEY_PAUSE: {
                keyCode = Keyboard.KEY_PAUSE;
                break;
            }
            case GLFW.GLFW_KEY_MINUS: {
                keyCode = Keyboard.KEY_MINUS;
                break;
            }
            case GLFW.GLFW_KEY_EQUAL: {
                keyCode = Keyboard.KEY_EQUALS;
                break;
            }
            case GLFW.GLFW_KEY_LEFT_BRACKET: {
                keyCode = Keyboard.KEY_LBRACKET;
                break;
            }
            case GLFW.GLFW_KEY_RIGHT_BRACKET: {
                keyCode = Keyboard.KEY_RBRACKET;
                break;
            }
            case GLFW.GLFW_KEY_APOSTROPHE: {
                keyCode = Keyboard.KEY_APOSTROPHE;
                break;
            }
            // public static final int KEY_AT = 0x91;
            /* (NEC PC98) */
            // public static final int KEY_COLON = 0x92;
            /* (NEC PC98) */
            // public static final int KEY_UNDERLINE = 0x93;
            /* (NEC PC98) */
            // public static final int KEY_KANA = 0x70;
            /* (Japanese keyboard) */
            // public static final int KEY_CONVERT = 0x79;
            /* (Japanese keyboard) */
            // public static final int KEY_NOCONVERT = 0x7B;
            /* (Japanese keyboard) */
            // public static final int KEY_YEN = 0x7D;
            /* (Japanese keyboard) */
            // public static final int KEY_CIRCUMFLEX = 0x90;
            /* (Japanese keyboard) */
            // public static final int KEY_KANJI = 0x94;
            /* (Japanese keyboard) */
            // public static final int KEY_STOP = 0x95;
            /* (NEC PC98) */
            // public static final int KEY_AX = 0x96;
            /* (Japan AX) */
            // public static final int KEY_UNLABELED = 0x97;
            /* (J3100) */
            // public static final int KEY_SECTION = 0xA7;
            /* Section symbol (Mac) */
            // public static final int KEY_NUMPADCOMMA = 0xB3;
            /* , on numeric keypad (NEC PC98) */
            // public static final int KEY_SYSRQ = 0xB7;
            // public static final int KEY_FUNCTION = 0xC4;
            /* Function (Mac) */
            // public static final int KEY_CLEAR = 0xDA;
            /* Clear key (Mac) */
            // public static final int KEY_APPS = 0xDD;
            /* AppMenu key */
            // public static final int KEY_POWER = 0xDE;
            // public static final int KEY_SLEEP = 0xDF;

            default: {
                keyCode = Keyboard.KEY_NONE;
                break;
            }
        }
        
        return keyCode;
    }

    public static int lwjglToGlfw(int lwjglKeyCode) {
        if (lwjglKeyCode > GLFW.GLFW_KEY_LAST) {
            return lwjglKeyCode;
        }
        
        int keyCode;
        switch (lwjglKeyCode) {
            case Keyboard.KEY_NONE: {
                keyCode = 0;
                break;
            }
            // arbitrary mapping to fix text input here
            case Keyboard.KEY_UNLABELED: {
                keyCode = GLFW.GLFW_KEY_UNKNOWN;
                break;
            }
            case Keyboard.KEY_ESCAPE: {
                keyCode = GLFW.GLFW_KEY_ESCAPE;
                break;
            }
            case Keyboard.KEY_BACK: {
                keyCode = GLFW.GLFW_KEY_BACKSPACE;
                break;
            }
            case Keyboard.KEY_TAB: {
                keyCode = GLFW.GLFW_KEY_TAB;
                break;
            }
            case Keyboard.KEY_RETURN: {
                keyCode = GLFW.GLFW_KEY_ENTER;
                break;
            }
            case Keyboard.KEY_SPACE: {
                keyCode = GLFW.GLFW_KEY_SPACE;
                break;
            }
            case Keyboard.KEY_LCONTROL: {
                keyCode = GLFW.GLFW_KEY_LEFT_CONTROL;
                break;
            }
            case Keyboard.KEY_LSHIFT: {
                keyCode = GLFW.GLFW_KEY_LEFT_SHIFT;
                break;
            }
            case Keyboard.KEY_LMENU: {
                keyCode = GLFW.GLFW_KEY_LEFT_ALT;
                break;
            }
            case Keyboard.KEY_LMETA: {
                keyCode = GLFW.GLFW_KEY_LEFT_SUPER;
                break;
            }
            case Keyboard.KEY_RCONTROL: {
                keyCode = GLFW.GLFW_KEY_RIGHT_CONTROL;
                break;
            }
            case Keyboard.KEY_RSHIFT: {
                keyCode = GLFW.GLFW_KEY_RIGHT_SHIFT;
                break;
            }
            case Keyboard.KEY_RMENU: {
                keyCode = GLFW.GLFW_KEY_RIGHT_ALT;
                break;
            }
            case Keyboard.KEY_RMETA: {
                keyCode = GLFW.GLFW_KEY_RIGHT_SUPER;
                break;
            }
            case Keyboard.KEY_1: {
                keyCode = GLFW.GLFW_KEY_1;
                break;
            }
            case Keyboard.KEY_2: {
                keyCode = GLFW.GLFW_KEY_2;
                break;
            }
            case Keyboard.KEY_3: {
                keyCode = GLFW.GLFW_KEY_3;
                break;
            }
            case Keyboard.KEY_4: {
                keyCode = GLFW.GLFW_KEY_4;
                break;
            }
            case Keyboard.KEY_5: {
                keyCode = GLFW.GLFW_KEY_5;
                break;
            }
            case Keyboard.KEY_6: {
                keyCode = GLFW.GLFW_KEY_6;
                break;
            }
            case Keyboard.KEY_7: {
                keyCode = GLFW.GLFW_KEY_7;
                break;
            }
            case Keyboard.KEY_8: {
                keyCode = GLFW.GLFW_KEY_8;
                break;
            }
            case Keyboard.KEY_9: {
                keyCode = GLFW.GLFW_KEY_9;
                break;
            }
            case Keyboard.KEY_0: {
                keyCode = GLFW.GLFW_KEY_0;
                break;
            }
            case Keyboard.KEY_A: {
                keyCode = GLFW.GLFW_KEY_A;
                break;
            }
            case Keyboard.KEY_B: {
                keyCode = GLFW.GLFW_KEY_B;
                break;
            }
            case Keyboard.KEY_C: {
                keyCode = GLFW.GLFW_KEY_C;
                break;
            }
            case Keyboard.KEY_D: {
                keyCode = GLFW.GLFW_KEY_D;
                break;
            }
            case Keyboard.KEY_E: {
                keyCode = GLFW.GLFW_KEY_E;
                break;
            }
            case Keyboard.KEY_F: {
                keyCode = GLFW.GLFW_KEY_F;
                break;
            }
            case Keyboard.KEY_G: {
                keyCode = GLFW.GLFW_KEY_G;
                break;
            }
            case Keyboard.KEY_H: {
                keyCode = GLFW.GLFW_KEY_H;
                break;
            }
            case Keyboard.KEY_I: {
                keyCode = GLFW.GLFW_KEY_I;
                break;
            }
            case Keyboard.KEY_J: {
                keyCode = GLFW.GLFW_KEY_J;
                break;
            }
            case Keyboard.KEY_K: {
                keyCode = GLFW.GLFW_KEY_K;
                break;
            }
            case Keyboard.KEY_L: {
                keyCode = GLFW.GLFW_KEY_L;
                break;
            }
            case Keyboard.KEY_M: {
                keyCode = GLFW.GLFW_KEY_M;
                break;
            }
            case Keyboard.KEY_N: {
                keyCode = GLFW.GLFW_KEY_N;
                break;
            }
            case Keyboard.KEY_O: {
                keyCode = GLFW.GLFW_KEY_O;
                break;
            }
            case Keyboard.KEY_P: {
                keyCode = GLFW.GLFW_KEY_P;
                break;
            }
            case Keyboard.KEY_Q: {
                keyCode = GLFW.GLFW_KEY_Q;
                break;
            }
            case Keyboard.KEY_R: {
                keyCode = GLFW.GLFW_KEY_R;
                break;
            }
            case Keyboard.KEY_S: {
                keyCode = GLFW.GLFW_KEY_S;
                break;
            }
            case Keyboard.KEY_T: {
                keyCode = GLFW.GLFW_KEY_T;
                break;
            }
            case Keyboard.KEY_U: {
                keyCode = GLFW.GLFW_KEY_U;
                break;
            }
            case Keyboard.KEY_V: {
                keyCode = GLFW.GLFW_KEY_V;
                break;
            }
            case Keyboard.KEY_W: {
                keyCode = GLFW.GLFW_KEY_W;
                break;
            }
            case Keyboard.KEY_X: {
                keyCode = GLFW.GLFW_KEY_X;
                break;
            }
            case Keyboard.KEY_Y: {
                keyCode = GLFW.GLFW_KEY_Y;
                break;
            }
            case Keyboard.KEY_Z: {
                keyCode = GLFW.GLFW_KEY_Z;
                break;
            }
            case Keyboard.KEY_UP: {
                keyCode = GLFW.GLFW_KEY_UP;
                break;
            }
            case Keyboard.KEY_DOWN: {
                keyCode = GLFW.GLFW_KEY_DOWN;
                break;
            }
            case Keyboard.KEY_LEFT: {
                keyCode = GLFW.GLFW_KEY_LEFT;
                break;
            }
            case Keyboard.KEY_RIGHT: {
                keyCode = GLFW.GLFW_KEY_RIGHT;
                break;
            }
            case Keyboard.KEY_INSERT: {
                keyCode = GLFW.GLFW_KEY_INSERT;
                break;
            }
            case Keyboard.KEY_DELETE: {
                keyCode = GLFW.GLFW_KEY_DELETE;
                break;
            }
            case Keyboard.KEY_HOME: {
                keyCode = GLFW.GLFW_KEY_HOME;
                break;
            }
            case Keyboard.KEY_END: {
                keyCode = GLFW.GLFW_KEY_END;
                break;
            }
            case Keyboard.KEY_PRIOR: {
                keyCode = GLFW.GLFW_KEY_PAGE_UP;
                break;
            }
            case Keyboard.KEY_NEXT: {
                keyCode = GLFW.GLFW_KEY_PAGE_DOWN;
                break;
            }
            case Keyboard.KEY_F1: {
                keyCode = GLFW.GLFW_KEY_F1;
                break;
            }
            case Keyboard.KEY_F2: {
                keyCode = GLFW.GLFW_KEY_F2;
                break;
            }
            case Keyboard.KEY_F3: {
                keyCode = GLFW.GLFW_KEY_F3;
                break;
            }
            case Keyboard.KEY_F4: {
                keyCode = GLFW.GLFW_KEY_F4;
                break;
            }
            case Keyboard.KEY_F5: {
                keyCode = GLFW.GLFW_KEY_F5;
                break;
            }
            case Keyboard.KEY_F6: {
                keyCode = GLFW.GLFW_KEY_F6;
                break;
            }
            case Keyboard.KEY_F7: {
                keyCode = GLFW.GLFW_KEY_F7;
                break;
            }
            case Keyboard.KEY_F8: {
                keyCode = GLFW.GLFW_KEY_F8;
                break;
            }
            case Keyboard.KEY_F9: {
                keyCode = GLFW.GLFW_KEY_F9;
                break;
            }
            case Keyboard.KEY_F10: {
                keyCode = GLFW.GLFW_KEY_F10;
                break;
            }
            case Keyboard.KEY_F11: {
                keyCode = GLFW.GLFW_KEY_F11;
                break;
            }
            case Keyboard.KEY_F12: {
                keyCode = GLFW.GLFW_KEY_F12;
                break;
            }
            case Keyboard.KEY_F13: {
                keyCode = GLFW.GLFW_KEY_F13;
                break;
            }
            case Keyboard.KEY_F14: {
                keyCode = GLFW.GLFW_KEY_F14;
                break;
            }
            case Keyboard.KEY_F15: {
                keyCode = GLFW.GLFW_KEY_F15;
                break;
            }
            case Keyboard.KEY_F16: {
                keyCode = GLFW.GLFW_KEY_F16;
                break;
            }
            case Keyboard.KEY_F17: {
                keyCode = GLFW.GLFW_KEY_F17;
                break;
            }
            case Keyboard.KEY_F18: {
                keyCode = GLFW.GLFW_KEY_F18;
                break;
            }
            case Keyboard.KEY_F19: {
                keyCode = GLFW.GLFW_KEY_F19;
                break;
            }
            case Keyboard.KEY_NUMPAD1: {
                keyCode = GLFW.GLFW_KEY_KP_1;
                break;
            }
            case Keyboard.KEY_NUMPAD2: {
                keyCode = GLFW.GLFW_KEY_KP_2;
                break;
            }
            case Keyboard.KEY_NUMPAD3: {
                keyCode = GLFW.GLFW_KEY_KP_3;
                break;
            }
            case Keyboard.KEY_NUMPAD4: {
                keyCode = GLFW.GLFW_KEY_KP_4;
                break;
            }
            case Keyboard.KEY_NUMPAD5: {
                keyCode = GLFW.GLFW_KEY_KP_5;
                break;
            }
            case Keyboard.KEY_NUMPAD6: {
                keyCode = GLFW.GLFW_KEY_KP_6;
                break;
            }
            case Keyboard.KEY_NUMPAD7: {
                keyCode = GLFW.GLFW_KEY_KP_7;
                break;
            }
            case Keyboard.KEY_NUMPAD8: {
                keyCode = GLFW.GLFW_KEY_KP_8;
                break;
            }
            case Keyboard.KEY_NUMPAD9: {
                keyCode = GLFW.GLFW_KEY_KP_9;
                break;
            }
            case Keyboard.KEY_NUMPAD0: {
                keyCode = GLFW.GLFW_KEY_KP_0;
                break;
            }
            case Keyboard.KEY_ADD: {
                keyCode = GLFW.GLFW_KEY_KP_ADD;
                break;
            }
            case Keyboard.KEY_SUBTRACT: {
                keyCode = GLFW.GLFW_KEY_KP_SUBTRACT;
                break;
            }
            case Keyboard.KEY_MULTIPLY: {
                keyCode = GLFW.GLFW_KEY_KP_MULTIPLY;
                break;
            }
            case Keyboard.KEY_DIVIDE: {
                keyCode = GLFW.GLFW_KEY_KP_DIVIDE;
                break;
            }
            case Keyboard.KEY_DECIMAL: {
                keyCode = GLFW.GLFW_KEY_KP_DECIMAL;
                break;
            }
            case Keyboard.KEY_NUMPADEQUALS: {
                keyCode = GLFW.GLFW_KEY_KP_EQUAL;
                break;
            }
            case Keyboard.KEY_NUMPADENTER: {
                keyCode = GLFW.GLFW_KEY_KP_ENTER;
                break;
            }
            case Keyboard.KEY_NUMLOCK: {
                keyCode = GLFW.GLFW_KEY_NUM_LOCK;
                break;
            }
            case Keyboard.KEY_SEMICOLON: {
                keyCode = GLFW.GLFW_KEY_SEMICOLON;
                break;
            }
            case Keyboard.KEY_BACKSLASH: {
                keyCode = GLFW.GLFW_KEY_BACKSLASH;
                break;
            }
            case Keyboard.KEY_COMMA: {
                keyCode = GLFW.GLFW_KEY_COMMA;
                break;
            }
            case Keyboard.KEY_PERIOD: {
                keyCode = GLFW.GLFW_KEY_PERIOD;
                break;
            }
            case Keyboard.KEY_SLASH: {
                keyCode = GLFW.GLFW_KEY_SLASH;
                break;
            }
            case Keyboard.KEY_GRAVE: {
                keyCode = GLFW.GLFW_KEY_GRAVE_ACCENT;
                break;
            }
            case Keyboard.KEY_CAPITAL: {
                keyCode = GLFW.GLFW_KEY_CAPS_LOCK;
                break;
            }
            case Keyboard.KEY_SCROLL: {
                keyCode = GLFW.GLFW_KEY_SCROLL_LOCK;
                break;
            }
            case Keyboard.KEY_PAUSE: {
                keyCode = GLFW.GLFW_KEY_PAUSE;
                break;
            }
            // "World" keys could be anything depending on
            // keyboard layout, pick something arbitrary
            case Keyboard.KEY_CIRCUMFLEX: {
                keyCode = GLFW.GLFW_KEY_WORLD_1;
                break;
            }
            case Keyboard.KEY_YEN: {
                keyCode = GLFW.GLFW_KEY_WORLD_2;
                break;
            }

            case Keyboard.KEY_MINUS: {
                keyCode = GLFW.GLFW_KEY_MINUS;
                break;
            }
            case Keyboard.KEY_EQUALS: {
                keyCode = GLFW.GLFW_KEY_EQUAL;
                break;
            }
            case Keyboard.KEY_LBRACKET: {
                keyCode = GLFW.GLFW_KEY_LEFT_BRACKET;
                break;
            }
            case Keyboard.KEY_RBRACKET: {
                keyCode = GLFW.GLFW_KEY_RIGHT_BRACKET;
                break;
            }
            case Keyboard.KEY_APOSTROPHE: {
                keyCode = GLFW.GLFW_KEY_APOSTROPHE;
                break;
            }
            // public static final int KEY_AT = 0x91;
            /* (NEC PC98) */
            // public static final int KEY_COLON = 0x92;
            /* (NEC PC98) */
            // public static final int KEY_UNDERLINE = 0x93;
            /* (NEC PC98) */
            // public static final int KEY_KANA = 0x70;
            /* (Japanese keyboard) */
            // public static final int KEY_CONVERT = 0x79;
            /* (Japanese keyboard) */
            // public static final int KEY_NOCONVERT = 0x7B;
            /* (Japanese keyboard) */
            // public static final int KEY_YEN = 0x7D;
            /* (Japanese keyboard) */
            // public static final int KEY_CIRCUMFLEX = 0x90;
            /* (Japanese keyboard) */
            // public static final int KEY_KANJI = 0x94;
            /* (Japanese keyboard) */
            // public static final int KEY_STOP = 0x95;
            /* (NEC PC98) */
            // public static final int KEY_AX = 0x96;
            /* (Japan AX) */
            // public static final int KEY_UNLABELED = 0x97;
            /* (J3100) */
            // public static final int KEY_SECTION = 0xA7;
            /* Section symbol (Mac) */
            // public static final int KEY_NUMPADCOMMA = 0xB3;
            /* , on numeric keypad (NEC PC98) */
            // public static final int KEY_SYSRQ = 0xB7;
            // public static final int KEY_FUNCTION = 0xC4;
            /* Function (Mac) */
            // public static final int KEY_CLEAR = 0xDA;
            /* Clear key (Mac) */
            // public static final int KEY_APPS = 0xDD;
            /* AppMenu key */
            // public static final int KEY_POWER = 0xDE;
            // public static final int KEY_SLEEP = 0xDF;
            default: {
                keyCode = GLFW.GLFW_KEY_UNKNOWN;
                break;
            }
        }
        
        return keyCode;
    }

    public static int awtToLwjgl(int awtCode) {
        int keyCode;
        switch (awtCode) {
            case KeyEvent.VK_ESCAPE: {
                keyCode = Keyboard.KEY_ESCAPE;
                break;
            }
            case KeyEvent.VK_1: {
                keyCode = Keyboard.KEY_1;
                break;
            }
            case KeyEvent.VK_2: {
                keyCode = Keyboard.KEY_2;
                break;
            }
            case KeyEvent.VK_3: {
                keyCode = Keyboard.KEY_3;
                break;
            }
            case KeyEvent.VK_4: {
                keyCode = Keyboard.KEY_4;
                break;
            }
            case KeyEvent.VK_5: {
                keyCode = Keyboard.KEY_5;
                break;
            }
            case KeyEvent.VK_6: {
                keyCode = Keyboard.KEY_6;
                break;
            }
            case KeyEvent.VK_7: {
                keyCode = Keyboard.KEY_7;
                break;
            }
            case KeyEvent.VK_8: {
                keyCode = Keyboard.KEY_8;
                break;
            }
            case KeyEvent.VK_9: {
                keyCode = Keyboard.KEY_9;
                break;
            }
            case KeyEvent.VK_0: {
                keyCode = Keyboard.KEY_0;
                break;
            }
            case KeyEvent.VK_MINUS: {
                keyCode = Keyboard.KEY_MINUS;
                break;
            }
            case KeyEvent.VK_EQUALS: {
                keyCode = Keyboard.KEY_EQUALS;
                break;
            }
            case KeyEvent.VK_BACK_SPACE: {
                keyCode = Keyboard.KEY_BACK;
                break;
            }
            case KeyEvent.VK_TAB: {
                keyCode = Keyboard.KEY_TAB;
                break;
            }
            case KeyEvent.VK_Q: {
                keyCode = Keyboard.KEY_Q;
                break;
            }
            case KeyEvent.VK_W: {
                keyCode = Keyboard.KEY_W;
                break;
            }
            case KeyEvent.VK_E: {
                keyCode = Keyboard.KEY_E;
                break;
            }
            case KeyEvent.VK_R: {
                keyCode = Keyboard.KEY_R;
                break;
            }
            case KeyEvent.VK_T: {
                keyCode = Keyboard.KEY_T;
                break;
            }
            case KeyEvent.VK_Y: {
                keyCode = Keyboard.KEY_Y;
                break;
            }
            case KeyEvent.VK_U: {
                keyCode = Keyboard.KEY_U;
                break;
            }
            case KeyEvent.VK_I: {
                keyCode = Keyboard.KEY_I;
                break;
            }
            case KeyEvent.VK_O: {
                keyCode = Keyboard.KEY_O;
                break;
            }
            case KeyEvent.VK_P: {
                keyCode = Keyboard.KEY_P;
                break;
            }
            case KeyEvent.VK_OPEN_BRACKET: {
                keyCode = Keyboard.KEY_LBRACKET;
                break;
            }
            case KeyEvent.VK_CLOSE_BRACKET: {
                keyCode = Keyboard.KEY_RBRACKET;
                break;
            }
            case KeyEvent.VK_ENTER: {
                keyCode = Keyboard.KEY_RETURN;
                break;
            }
            case KeyEvent.VK_CONTROL: {
                keyCode = Keyboard.KEY_LCONTROL;
                break;
            }
            case KeyEvent.VK_A: {
                keyCode = Keyboard.KEY_A;
                break;
            }
            case KeyEvent.VK_S: {
                keyCode = Keyboard.KEY_S;
                break;
            }
            case KeyEvent.VK_D: {
                keyCode = Keyboard.KEY_D;
                break;
            }
            case KeyEvent.VK_F: {
                keyCode = Keyboard.KEY_F;
                break;
            }
            case KeyEvent.VK_G: {
                keyCode = Keyboard.KEY_G;
                break;
            }
            case KeyEvent.VK_H: {
                keyCode = Keyboard.KEY_H;
                break;
            }
            case KeyEvent.VK_J: {
                keyCode = Keyboard.KEY_J;
                break;
            }
            case KeyEvent.VK_K: {
                keyCode = Keyboard.KEY_K;
                break;
            }
            case KeyEvent.VK_L: {
                keyCode = Keyboard.KEY_L;
                break;
            }
            case KeyEvent.VK_SEMICOLON: {
                keyCode = Keyboard.KEY_SEMICOLON;
                break;
            }
            case KeyEvent.VK_QUOTE: {
                keyCode = Keyboard.KEY_APOSTROPHE;
                break;
            }
            case KeyEvent.VK_DEAD_GRAVE: {
                keyCode = Keyboard.KEY_GRAVE;
                break;
            }
            case KeyEvent.VK_SHIFT: {
                keyCode = Keyboard.KEY_LSHIFT;
                break;
            }
            case KeyEvent.VK_BACK_SLASH: {
                keyCode = Keyboard.KEY_BACKSLASH;
                break;
            }
            case KeyEvent.VK_Z: {
                keyCode = Keyboard.KEY_Z;
                break;
            }
            case KeyEvent.VK_X: {
                keyCode = Keyboard.KEY_X;
                break;
            }
            case KeyEvent.VK_C: {
                keyCode = Keyboard.KEY_C;
                break;
            }
            case KeyEvent.VK_V: {
                keyCode = Keyboard.KEY_V;
                break;
            }
            case KeyEvent.VK_B: {
                keyCode = Keyboard.KEY_B;
                break;
            }
            case KeyEvent.VK_N: {
                keyCode = Keyboard.KEY_N;
                break;
            }
            case KeyEvent.VK_M: {
                keyCode = Keyboard.KEY_M;
                break;
            }
            case KeyEvent.VK_COMMA: {
                keyCode = Keyboard.KEY_COMMA;
                break;
            }
            case KeyEvent.VK_PERIOD: {
                keyCode = Keyboard.KEY_PERIOD;
                break;
            }
            case KeyEvent.VK_SLASH: {
                keyCode = Keyboard.KEY_SLASH;
                break;
            }
            case KeyEvent.VK_MULTIPLY: {
                keyCode = Keyboard.KEY_MULTIPLY;
                break;
            }
            case KeyEvent.VK_ALT: {
                keyCode = Keyboard.KEY_LMENU;
                break;
            }
            case KeyEvent.VK_SPACE: {
                keyCode = Keyboard.KEY_SPACE;
                break;
            }
            case KeyEvent.VK_CAPS_LOCK: {
                keyCode = Keyboard.KEY_CAPITAL;
                break;
            }
            case KeyEvent.VK_F1: {
                keyCode = Keyboard.KEY_F1;
                break;
            }
            case KeyEvent.VK_F2: {
                keyCode = Keyboard.KEY_F2;
                break;
            }
            case KeyEvent.VK_F3: {
                keyCode = Keyboard.KEY_F3;
                break;
            }
            case KeyEvent.VK_F4: {
                keyCode = Keyboard.KEY_F4;
                break;
            }
            case KeyEvent.VK_F5: {
                keyCode = Keyboard.KEY_F5;
                break;
            }
            case KeyEvent.VK_F6: {
                keyCode = Keyboard.KEY_F6;
                break;
            }
            case KeyEvent.VK_F7: {
                keyCode = Keyboard.KEY_F7;
                break;
            }
            case KeyEvent.VK_F8: {
                keyCode = Keyboard.KEY_F8;
                break;
            }
            case KeyEvent.VK_F9: {
                keyCode = Keyboard.KEY_F9;
                break;
            }
            case KeyEvent.VK_F10: {
                keyCode = Keyboard.KEY_F10;
                break;
            }
            case KeyEvent.VK_NUM_LOCK: {
                keyCode = Keyboard.KEY_NUMLOCK;
                break;
            }
            case KeyEvent.VK_SCROLL_LOCK: {
                keyCode = Keyboard.KEY_SCROLL;
                break;
            }
            case KeyEvent.VK_NUMPAD7: {
                keyCode = Keyboard.KEY_NUMPAD7;
                break;
            }
            case KeyEvent.VK_NUMPAD8: {
                keyCode = Keyboard.KEY_NUMPAD8;
                break;
            }
            case KeyEvent.VK_NUMPAD9: {
                keyCode = Keyboard.KEY_NUMPAD9;
                break;
            }
            case KeyEvent.VK_SUBTRACT: {
                keyCode = Keyboard.KEY_SUBTRACT;
                break;
            }
            case KeyEvent.VK_NUMPAD4: {
                keyCode = Keyboard.KEY_NUMPAD4;
                break;
            }
            case KeyEvent.VK_NUMPAD5: {
                keyCode = Keyboard.KEY_NUMPAD5;
                break;
            }
            case KeyEvent.VK_NUMPAD6: {
                keyCode = Keyboard.KEY_NUMPAD6;
                break;
            }
            case KeyEvent.VK_ADD: {
                keyCode = Keyboard.KEY_ADD;
                break;
            }
            case KeyEvent.VK_NUMPAD1: {
                keyCode = Keyboard.KEY_NUMPAD1;
                break;
            }
            case KeyEvent.VK_NUMPAD2: {
                keyCode = Keyboard.KEY_NUMPAD2;
                break;
            }
            case KeyEvent.VK_NUMPAD3: {
                keyCode = Keyboard.KEY_NUMPAD3;
                break;
            }
            case KeyEvent.VK_NUMPAD0: {
                keyCode = Keyboard.KEY_NUMPAD0;
                break;
            }
            case KeyEvent.VK_DECIMAL: {
                keyCode = Keyboard.KEY_DECIMAL;
                break;
            }
            case KeyEvent.VK_F11: {
                keyCode = Keyboard.KEY_F11;
                break;
            }
            case KeyEvent.VK_F12: {
                keyCode = Keyboard.KEY_F12;
                break;
            }
            case KeyEvent.VK_F13: {
                keyCode = Keyboard.KEY_F13;
                break;
            }
            case KeyEvent.VK_F14: {
                keyCode = Keyboard.KEY_F14;
                break;
            }
            case KeyEvent.VK_F15: {
                keyCode = Keyboard.KEY_F15;
                break;
            }
            case KeyEvent.VK_KANA: {
                keyCode = Keyboard.KEY_KANA;
                break;
            }
            case KeyEvent.VK_CONVERT: {
                keyCode = Keyboard.KEY_CONVERT;
                break;
            }
            case KeyEvent.VK_NONCONVERT: {
                keyCode = Keyboard.KEY_NOCONVERT;
                break;
            }
            case KeyEvent.VK_CIRCUMFLEX: {
                keyCode = Keyboard.KEY_CIRCUMFLEX;
                break;
            }
            case KeyEvent.VK_AT: {
                keyCode = Keyboard.KEY_AT;
                break;
            }
            case KeyEvent.VK_COLON: {
                keyCode = Keyboard.KEY_COLON;
                break;
            }
            case KeyEvent.VK_UNDERSCORE: {
                keyCode = Keyboard.KEY_UNDERLINE;
                break;
            }
            case KeyEvent.VK_KANJI: {
                keyCode = Keyboard.KEY_KANJI;
                break;
            }
            case KeyEvent.VK_STOP: {
                keyCode = Keyboard.KEY_STOP;
                break;
            }
            case KeyEvent.VK_DIVIDE: {
                keyCode = Keyboard.KEY_DIVIDE;
                break;
            }
            case KeyEvent.VK_PAUSE: {
                keyCode = Keyboard.KEY_PAUSE;
                break;
            }
            case KeyEvent.VK_HOME: {
                keyCode = Keyboard.KEY_HOME;
                break;
            }
            case KeyEvent.VK_UP: {
                keyCode = Keyboard.KEY_UP;
                break;
            }
            case KeyEvent.VK_PAGE_UP: {
                keyCode = Keyboard.KEY_PRIOR;
                break;
            }
            case KeyEvent.VK_LEFT: {
                keyCode = Keyboard.KEY_LEFT;
                break;
            }
            case KeyEvent.VK_RIGHT: {
                keyCode = Keyboard.KEY_RIGHT;
                break;
            }
            case KeyEvent.VK_END: {
                keyCode = Keyboard.KEY_END;
                break;
            }
            case KeyEvent.VK_DOWN: {
                keyCode = Keyboard.KEY_DOWN;
                break;
            }
            case KeyEvent.VK_PAGE_DOWN: {
                keyCode = Keyboard.KEY_NEXT;
                break;
            }
            case KeyEvent.VK_INSERT: {
                keyCode = Keyboard.KEY_INSERT;
                break;
            }
            case KeyEvent.VK_DELETE: {
                keyCode = Keyboard.KEY_DELETE;
                break;
            }
            case KeyEvent.VK_META: {
                keyCode = Keyboard.KEY_LWIN;
                break;
            }
            default: {
                keyCode = Keyboard.KEY_NONE;
                break;
            }
        }
        
        return keyCode;
    }

    public static int lwjglToAwt(int lwjglCode) {
        int keyCode;
        switch (lwjglCode) {
            case Keyboard.KEY_ESCAPE: {
                keyCode = KeyEvent.VK_ESCAPE;
                break;
            }
            case Keyboard.KEY_1: {
                keyCode = KeyEvent.VK_1;
                break;
            }
            case Keyboard.KEY_2: {
                keyCode = KeyEvent.VK_2;
                break;
            }
            case Keyboard.KEY_3: {
                keyCode = KeyEvent.VK_3;
                break;
            }
            case Keyboard.KEY_4: {
                keyCode = KeyEvent.VK_4;
                break;
            }
            case Keyboard.KEY_5: {
                keyCode = KeyEvent.VK_5;
                break;
            }
            case Keyboard.KEY_6: {
                keyCode = KeyEvent.VK_6;
                break;
            }
            case Keyboard.KEY_7: {
                keyCode = KeyEvent.VK_7;
                break;
            }
            case Keyboard.KEY_8: {
                keyCode = KeyEvent.VK_8;
                break;
            }
            case Keyboard.KEY_9: {
                keyCode = KeyEvent.VK_9;
                break;
            }
            case Keyboard.KEY_0: {
                keyCode = KeyEvent.VK_0;
                break;
            }
            case Keyboard.KEY_MINUS: {
                keyCode = KeyEvent.VK_MINUS;
                break;
            }
            case Keyboard.KEY_EQUALS: {
                keyCode = KeyEvent.VK_EQUALS;
                break;
            }
            case Keyboard.KEY_BACK: {
                keyCode = KeyEvent.VK_BACK_SPACE;
                break;
            }
            case Keyboard.KEY_TAB: {
                keyCode = KeyEvent.VK_TAB;
                break;
            }
            case Keyboard.KEY_Q: {
                keyCode = KeyEvent.VK_Q;
                break;
            }
            case Keyboard.KEY_W: {
                keyCode = KeyEvent.VK_W;
                break;
            }
            case Keyboard.KEY_E: {
                keyCode = KeyEvent.VK_E;
                break;
            }
            case Keyboard.KEY_R: {
                keyCode = KeyEvent.VK_R;
                break;
            }
            case Keyboard.KEY_T: {
                keyCode = KeyEvent.VK_T;
                break;
            }
            case Keyboard.KEY_Y: {
                keyCode = KeyEvent.VK_Y;
                break;
            }
            case Keyboard.KEY_U: {
                keyCode = KeyEvent.VK_U;
                break;
            }
            case Keyboard.KEY_I: {
                keyCode = KeyEvent.VK_I;
                break;
            }
            case Keyboard.KEY_O: {
                keyCode = KeyEvent.VK_O;
                break;
            }
            case Keyboard.KEY_P: {
                keyCode = KeyEvent.VK_P;
                break;
            }
            case Keyboard.KEY_LBRACKET: {
                keyCode = KeyEvent.VK_OPEN_BRACKET;
                break;
            }
            case Keyboard.KEY_RBRACKET: {
                keyCode = KeyEvent.VK_CLOSE_BRACKET;
                break;
            }
            case Keyboard.KEY_RETURN: {
                keyCode = KeyEvent.VK_ENTER;
                break;
            }
            case Keyboard.KEY_LCONTROL: {
                keyCode = KeyEvent.VK_CONTROL;
                break;
            }
            case Keyboard.KEY_A: {
                keyCode = KeyEvent.VK_A;
                break;
            }
            case Keyboard.KEY_S: {
                keyCode = KeyEvent.VK_S;
                break;
            }
            case Keyboard.KEY_D: {
                keyCode = KeyEvent.VK_D;
                break;
            }
            case Keyboard.KEY_F: {
                keyCode = KeyEvent.VK_F;
                break;
            }
            case Keyboard.KEY_G: {
                keyCode = KeyEvent.VK_G;
                break;
            }
            case Keyboard.KEY_H: {
                keyCode = KeyEvent.VK_H;
                break;
            }
            case Keyboard.KEY_J: {
                keyCode = KeyEvent.VK_J;
                break;
            }
            case Keyboard.KEY_K: {
                keyCode = KeyEvent.VK_K;
                break;
            }
            case Keyboard.KEY_L: {
                keyCode = KeyEvent.VK_L;
                break;
            }
            case Keyboard.KEY_SEMICOLON: {
                keyCode = KeyEvent.VK_SEMICOLON;
                break;
            }
            case Keyboard.KEY_APOSTROPHE: {
                keyCode = KeyEvent.VK_QUOTE;
                break;
            }
            case Keyboard.KEY_GRAVE: {
                keyCode = KeyEvent.VK_DEAD_GRAVE;
                break;
            }
            case Keyboard.KEY_LSHIFT: {
                keyCode = KeyEvent.VK_SHIFT;
                break;
            }
            case Keyboard.KEY_BACKSLASH: {
                keyCode = KeyEvent.VK_BACK_SLASH;
                break;
            }
            case Keyboard.KEY_Z: {
                keyCode = KeyEvent.VK_Z;
                break;
            }
            case Keyboard.KEY_X: {
                keyCode = KeyEvent.VK_X;
                break;
            }
            case Keyboard.KEY_C: {
                keyCode = KeyEvent.VK_C;
                break;
            }
            case Keyboard.KEY_V: {
                keyCode = KeyEvent.VK_V;
                break;
            }
            case Keyboard.KEY_B: {
                keyCode = KeyEvent.VK_B;
                break;
            }
            case Keyboard.KEY_N: {
                keyCode = KeyEvent.VK_N;
                break;
            }
            case Keyboard.KEY_M: {
                keyCode = KeyEvent.VK_M;
                break;
            }
            case Keyboard.KEY_COMMA: {
                keyCode = KeyEvent.VK_COMMA;
                break;
            }
            case Keyboard.KEY_PERIOD: {
                keyCode = KeyEvent.VK_PERIOD;
                break;
            }
            case Keyboard.KEY_SLASH: {
                keyCode = KeyEvent.VK_SLASH;
                break;
            }
            case Keyboard.KEY_MULTIPLY: {
                keyCode = KeyEvent.VK_MULTIPLY;
                break;
            }
            case Keyboard.KEY_LMENU: {
                keyCode = KeyEvent.VK_ALT;
                break;
            }
            case Keyboard.KEY_SPACE: {
                keyCode = KeyEvent.VK_SPACE;
                break;
            }
            case Keyboard.KEY_CAPITAL: {
                keyCode = KeyEvent.VK_CAPS_LOCK;
                break;
            }
            case Keyboard.KEY_F1: {
                keyCode = KeyEvent.VK_F1;
                break;
            }
            case Keyboard.KEY_F2: {
                keyCode = KeyEvent.VK_F2;
                break;
            }
            case Keyboard.KEY_F3: {
                keyCode = KeyEvent.VK_F3;
                break;
            }
            case Keyboard.KEY_F4: {
                keyCode = KeyEvent.VK_F4;
                break;
            }
            case Keyboard.KEY_F5: {
                keyCode = KeyEvent.VK_F5;
                break;
            }
            case Keyboard.KEY_F6: {
                keyCode = KeyEvent.VK_F6;
                break;
            }
            case Keyboard.KEY_F7: {
                keyCode = KeyEvent.VK_F7;
                break;
            }
            case Keyboard.KEY_F8: {
                keyCode = KeyEvent.VK_F8;
                break;
            }
            case Keyboard.KEY_F9: {
                keyCode = KeyEvent.VK_F9;
                break;
            }
            case Keyboard.KEY_F10: {
                keyCode = KeyEvent.VK_F10;
                break;
            }
            case Keyboard.KEY_NUMLOCK: {
                keyCode = KeyEvent.VK_NUM_LOCK;
                break;
            }
            case Keyboard.KEY_SCROLL: {
                keyCode = KeyEvent.VK_SCROLL_LOCK;
                break;
            }
            case Keyboard.KEY_NUMPAD7: {
                keyCode = KeyEvent.VK_NUMPAD7;
                break;
            }
            case Keyboard.KEY_NUMPAD8: {
                keyCode = KeyEvent.VK_NUMPAD8;
                break;
            }
            case Keyboard.KEY_NUMPAD9: {
                keyCode = KeyEvent.VK_NUMPAD9;
                break;
            }
            case Keyboard.KEY_SUBTRACT: {
                keyCode = KeyEvent.VK_SUBTRACT;
                break;
            }
            case Keyboard.KEY_NUMPAD4: {
                keyCode = KeyEvent.VK_NUMPAD4;
                break;
            }
            case Keyboard.KEY_NUMPAD5: {
                keyCode = KeyEvent.VK_NUMPAD5;
                break;
            }
            case Keyboard.KEY_NUMPAD6: {
                keyCode = KeyEvent.VK_NUMPAD6;
                break;
            }
            case Keyboard.KEY_ADD: {
                keyCode = KeyEvent.VK_ADD;
                break;
            }
            case Keyboard.KEY_NUMPAD1: {
                keyCode = KeyEvent.VK_NUMPAD1;
                break;
            }
            case Keyboard.KEY_NUMPAD2: {
                keyCode = KeyEvent.VK_NUMPAD2;
                break;
            }
            case Keyboard.KEY_NUMPAD3: {
                keyCode = KeyEvent.VK_NUMPAD3;
                break;
            }
            case Keyboard.KEY_NUMPAD0: {
                keyCode = KeyEvent.VK_NUMPAD0;
                break;
            }
            case Keyboard.KEY_DECIMAL: {
                keyCode = KeyEvent.VK_DECIMAL;
                break;
            }
            case Keyboard.KEY_F11: {
                keyCode = KeyEvent.VK_F11;
                break;
            }
            case Keyboard.KEY_F12: {
                keyCode = KeyEvent.VK_F12;
                break;
            }
            case Keyboard.KEY_F13: {
                keyCode = KeyEvent.VK_F13;
                break;
            }
            case Keyboard.KEY_F14: {
                keyCode = KeyEvent.VK_F14;
                break;
            }
            case Keyboard.KEY_F15: {
                keyCode = KeyEvent.VK_F15;
                break;
            }
            case Keyboard.KEY_KANA: {
                keyCode = KeyEvent.VK_KANA;
                break;
            }
            case Keyboard.KEY_CONVERT: {
                keyCode = KeyEvent.VK_CONVERT;
                break;
            }
            case Keyboard.KEY_NOCONVERT: {
                keyCode = KeyEvent.VK_NONCONVERT;
                break;
            }
            case Keyboard.KEY_CIRCUMFLEX: {
                keyCode = KeyEvent.VK_CIRCUMFLEX;
                break;
            }
            case Keyboard.KEY_AT: {
                keyCode = KeyEvent.VK_AT;
                break;
            }
            case Keyboard.KEY_COLON: {
                keyCode = KeyEvent.VK_COLON;
                break;
            }
            case Keyboard.KEY_UNDERLINE: {
                keyCode = KeyEvent.VK_UNDERSCORE;
                break;
            }
            case Keyboard.KEY_KANJI: {
                keyCode = KeyEvent.VK_KANJI;
                break;
            }
            case Keyboard.KEY_STOP: {
                keyCode = KeyEvent.VK_STOP;
                break;
            }
            case Keyboard.KEY_DIVIDE: {
                keyCode = KeyEvent.VK_DIVIDE;
                break;
            }
            case Keyboard.KEY_PAUSE: {
                keyCode = KeyEvent.VK_PAUSE;
                break;
            }
            case Keyboard.KEY_HOME: {
                keyCode = KeyEvent.VK_HOME;
                break;
            }
            case Keyboard.KEY_UP: {
                keyCode = KeyEvent.VK_UP;
                break;
            }
            case Keyboard.KEY_PRIOR: {
                keyCode = KeyEvent.VK_PAGE_UP;
                break;
            }
            case Keyboard.KEY_LEFT: {
                keyCode = KeyEvent.VK_LEFT;
                break;
            }
            case Keyboard.KEY_RIGHT: {
                keyCode = KeyEvent.VK_RIGHT;
                break;
            }
            case Keyboard.KEY_END: {
                keyCode = KeyEvent.VK_END;
                break;
            }
            case Keyboard.KEY_DOWN: {
                keyCode = KeyEvent.VK_DOWN;
                break;
            }
            case Keyboard.KEY_NEXT: {
                keyCode = KeyEvent.VK_PAGE_DOWN;
                break;
            }
            case Keyboard.KEY_INSERT: {
                keyCode = KeyEvent.VK_INSERT;
                break;
            }
            case Keyboard.KEY_DELETE: {
                keyCode = KeyEvent.VK_DELETE;
                break;
            }
            case Keyboard.KEY_LWIN: {
                keyCode = KeyEvent.VK_META;
                break;
            }
            default: {
                keyCode = Keyboard.KEY_NONE;
                break;
            }
        }

        return keyCode;
    }
}
