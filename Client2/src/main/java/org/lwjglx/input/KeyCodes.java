package org.lwjglx.input;

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
            default: {
                keyCode = GLFW.GLFW_KEY_UNKNOWN;
                break;
            }
        }
        
        return keyCode;
    }
}
