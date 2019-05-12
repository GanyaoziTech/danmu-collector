package tech.ganyaozi.danmu.colloctor.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Derek.p.dai@qq.com
 */
public class ConsoleTool {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleTool.class);

    private static final String BANNER = "================================================================";
    private static final String BORDER = "|";
    private static final String SEPARATOR = " : ";
    /**
     * 向左缩进的字符数
     */
    private static final int DEFAULT_INDENT = 4;

    private static final String[] DEFAULT_QUIT_CMD = {"quit", "q", "exit"};

    private static final BufferedReader BUFFERED_READER = new BufferedReader(new InputStreamReader(System.in));

    /**
     * @param list         Strings to be shown
     * @param addSerialNum if need to show serial number before section
     * @return index the number scanned in, or -1 if input is invalid
     */
    public static int ShowInConsole(ArrayList<String> list, boolean addSerialNum) {
        try {
            showTables(list, addSerialNum);

            String order = BUFFERED_READER.readLine();
            if (order == null) {
                return -1;
            }
            if (StringUtils.isNumeric(order)) {
                int orderInt = Integer.valueOf(order);
                if (orderInt < list.size() && orderInt > -1) {
                    logger.info("\n\n");
                    return orderInt;
                } else {
                    logger.error("Invalid number ! please input the number before selection \n");
                    return -1;
                }
            } else if (Arrays.binarySearch(DEFAULT_QUIT_CMD, order) > 0) {
                return -1;
            } else {
                logger.error("Invalid number ! please input the number before selection \n");
                return -1;
            }
        } catch (NumberFormatException e) {
            logger.error("Number is too large");
        } catch (IllegalArgumentException | IOException e) {
            logger.error("", e);
        }
        return -1;
    }

    public static void showTables(ArrayList<String> list, boolean AddNum) {
        logger.info(BANNER);
        for (int i = 0; i < list.size(); i++) {
            StringBuilder name;
            if (AddNum) {
                name = new StringBuilder(i + SEPARATOR + list.get(i));
            } else {
                name = new StringBuilder(list.get(i));
            }
            int rightSpace = BANNER.length() - name.toString().length() - DEFAULT_INDENT - 2;
            String line = BORDER + mkSpace(DEFAULT_INDENT) + name.toString() + mkSpace(rightSpace) + BORDER;
            logger.info(line);
        }
        logger.info(BANNER);
        logger.info("\n->");
    }

    private static String mkSpace(int i) {
        StringBuilder space = new StringBuilder();
        for (; i > 0; i--) {
            space.append(" ");
        }
        return space.toString();
    }


    public static String readLineUntilNotNull() {
        String input = null;
        do {
            try {
                input = BUFFERED_READER.readLine();
                if (input == null) {
                    logger.error("invalid input : null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (input == null);
        return input;

    }

    public static int readIntUntilNotNull() {
        int input = -1;
        String temp;
        do {
            try {
                temp = BUFFERED_READER.readLine();
                if (temp == null) {
                    logger.error("invalid input : null");
                    continue;
                }
                if (StringUtils.isNumeric(temp)) {
                    input = Integer.valueOf(temp);
                } else {
                    logger.error("Invalid input : {} , Please type in a valid int", temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (input == -1);

        return input;
    }

    public static void displayOneLine(String message) {
        logger.info(message);
    }
}
