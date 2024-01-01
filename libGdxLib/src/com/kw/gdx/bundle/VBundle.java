package com.kw.gdx.bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @Auther jian xian si qi
 * @Date 2024/1/1 22:02
 */
public class VBundle {
    private TextFormatter formatter;
    private I18NBundle bundle = null;
    private String prefLanguage = "auto";

    public VBundle() {
        formatter = new TextFormatter(GameLocale.getGameLocale().getDefault(), true);
    }

    public final String get(String key) {
        return getString(key);
    }

    public String format(String key, Object... args) {
        return formatter.format(get(key), args);
    }

    private boolean isNum(String txt) {
        try {
            Integer.parseInt(txt);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public String getString(String key) {
        if (isNum(key))
            return key;
        String language = GameLocale.getGameLocale().getLanguage();
        if (language == null)
            language = "auto";
        String out = null;
        if (bundle == null || !language.equals(prefLanguage)) {
            try {
                FileHandle baseFileHandle = Gdx.files.internal("");
                if (language.equals("auto")) {
                    bundle = I18NBundle.createBundle(baseFileHandle, GameLocale.getGameLocale().getDefault());
                } else {
                    bundle = I18NBundle.createBundle(baseFileHandle, new Locale(language));
                }
                out = bundle.get(key);
            } catch (Exception e) {
            }
        } else {
            try {
                out = bundle.get(key);
            } catch (Exception e) {
            }
        }
        prefLanguage = language;
        return out == null ? key : out;
    }

    class TextFormatter {
        private MessageFormat messageFormat;
        private StringBuilder buffer;

        public TextFormatter(Locale locale, boolean useMessageFormat) {
            buffer = new StringBuilder();
            if (useMessageFormat)
                messageFormat = new MessageFormat("", locale);
        }

        public String format(String pattern, Object... args) {
            if (messageFormat != null) {
                messageFormat.applyPattern(replaceEscapeChars(pattern));
                return messageFormat.format(args);
            }
            return simpleFormat(pattern, args);
        }

        private String replaceEscapeChars(String pattern) {
            buffer.setLength(0);
            boolean changed = false;
            int len = pattern.length();
            for (int i = 0; i < len; i++) {
                char ch = pattern.charAt(i);
                if (ch == '\'') {
                    changed = true;
                    buffer.append("''");
                } else if (ch == '{') {
                    int j = i + 1;
                    while (j < len && pattern.charAt(j) == '{')
                        j++;
                    int escaped = (j - i) / 2;
                    if (escaped > 0) {
                        changed = true;
                        buffer.append('\'');
                        do {
                            buffer.append('{');
                        } while ((--escaped) > 0);
                        buffer.append('\'');
                    }
                    if ((j - i) % 2 != 0)
                        buffer.append('{');
                    i = j - 1;
                } else {
                    buffer.append(ch);
                }
            }
            return changed ? buffer.toString() : pattern;
        }

        private String simpleFormat(String pattern, Object... args) {
            buffer.setLength(0);
            boolean changed = false;
            int placeholder = -1;
            int patternLength = pattern.length();
            for (int i = 0; i < patternLength; ++i) {
                char ch = pattern.charAt(i);
                if (placeholder < 0) { // processing constant part
                    if (ch == '{') {
                        changed = true;
                        if (i + 1 < patternLength
                                && pattern.charAt(i + 1) == '{') {
                            buffer.append(ch); // handle escaped '{'
                            ++i;
                        } else {
                            placeholder = 0; // switch to placeholder part
                        }
                    } else {
                        buffer.append(ch);
                    }
                } else { // processing placeholder part
                    if (ch == '}') {
                        if (placeholder >= args.length)
                            throw new IllegalArgumentException(
                                    "Argument index out of bounds: "
                                            + placeholder);
                        if (pattern.charAt(i - 1) == '{')
                            throw new IllegalArgumentException(
                                    "Missing argument index after a left curly brace");
                        if (args[placeholder] == null)
                            buffer.append("null"); // append null argument
                        else
                            buffer.append(args[placeholder].toString()); // append
                        placeholder = -1; // switch to constant part
                    } else {
                        if (ch < '0' || ch > '9')
                            throw new IllegalArgumentException("Unexpected '"
                                    + ch + "' while parsing argument index");
                        placeholder = placeholder * 10 + (ch - '0');
                    }
                }
            }
            if (placeholder >= 0)
                throw new IllegalArgumentException(
                        "Unmatched braces in the pattern.");

            return changed ? buffer.toString() : pattern;
        }
    }
}
