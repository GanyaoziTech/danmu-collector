package tech.ganyaozi.danmu.colloctor.bean;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据包结构说明
 * 00 00 00 28/00 10/00 00    00 00 00 07/00 00 00 00
 * 1-4 字节: 数据包长度
 * 5-6 字节: 协议头长度, 固定值 0x10
 * 7-8 字节: 设备类型, Android 固定为 0
 * 9-12 字节: 数据包类型
 * 13-16 字节: 设备类型, 同 7-8 字节
 * 之后的字节为数据包正文, 大多数情况下为 JSON
 */
@Data
public class BiliBiliMessage {

    private static final Logger loggerException = LoggerFactory.getLogger(BiliBiliMessage.class);

    public static final short PROTOCOL_HEAD_LENGTH = 16;

    private final int packageLength;
    private final short protocolHeadLength;
    private final DeviceType shortDeviceType;
    private final Action action;
    private final DeviceType longDeviceType;
    private final byte[] content;

    public BiliBiliMessage(int packageLength, short protocolHeadLength, DeviceType shortDeviceType, Action action, DeviceType longDeviceType, byte[] content) {
        this.packageLength = packageLength;
        this.protocolHeadLength = protocolHeadLength;
        this.shortDeviceType = shortDeviceType;
        this.action = action;
        this.longDeviceType = longDeviceType;
        this.content = content;
    }

    public BiliBiliMessage(Action action, byte[] content) {
        this(PROTOCOL_HEAD_LENGTH + content.length,
                PROTOCOL_HEAD_LENGTH,
                DeviceType.ANDROID,
                action,
                DeviceType.ANDROID,
                content
        );
    }

    public enum DeviceType {

        ANDROID(0x00);

        private final int value;

        DeviceType(int value) {
            this.value = value;
        }

        public static DeviceType valueOf(int value) {
            for (DeviceType deviceType : DeviceType.values()) {
                if (deviceType.value == value) {
                    return deviceType;
                }
            }
            loggerException.error("invalid device type : {} ", value);
            return ANDROID;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Action {
        HEART_BEAT(0x02),
        VIEWER_COUNT(0x03),
        MESSAGE(0x05),
        ENTER_ROOM(0x07),
        ENTER_ROOM_SUCCESS(0x08),
        UNKNOWN(0x99);

        private final int value;

        Action(int value) {
            this.value = value;
        }

        public static Action valueOf(int value) {
            for (Action action : Action.values()) {
                if (action.value == value) {
                    return action;
                }
            }
            loggerException.error("invalid action : {} ", value);
            return UNKNOWN;
        }
        public int getValue() {
            return value;
        }
    }

}
