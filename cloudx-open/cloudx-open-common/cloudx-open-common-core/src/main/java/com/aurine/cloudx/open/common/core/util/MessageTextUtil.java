package com.aurine.cloudx.open.common.core.util;

/**
 * <p>
 * 消息推送内容编辑辅助工具
 * </p>
 * @author : 王良俊
 * @date : 2021-07-21 15:24:47
 */

public class MessageTextUtil {

    public static MessageText init() {
        return new MessageText();
    }

    public static class MessageText {

        private final StringBuilder text;

        private MessageText() {
            this.text = new StringBuilder();
        }

        /**
         * <p>
         * 此处换行
         * </p>
         *
         */
        public MessageText br() {
            this.text.append("<br>");
            return this;
        }

        /**
         * <p>
         * 段落（前后换行）
         * </p>
         *
         */
        public MessageText p(String text) {
            return br().append(text);
        }

        /**
         * <p>
         * 段落（前后换行）
         * </p>
         *
         */
        public MessageText p(String format, Object... args) {
            return br().append(format, args);
        }

        /**
         * <p>
         * 添加文本 不换行
         * </p>
         *
         */
        public MessageText append(String text) {
            this.text.append(text);
            return this;
        }

        /**
         * <p>
         * 添加文本 不换行
         * </p>
         *
         */
        public MessageText append(String format, Object... args) {
            this.text.append(String.format(format, args));
            return this;
        }

        /**
         * <p>
         * 添加空格
         * </p>
         *
         * @param num 空格数
         */
        public MessageText nbsp(int num) {
            return append(String.format("%"+ num + "s", ""));
        }

        @Override
        public String toString() {
            return text.toString();
        }

    }

}

