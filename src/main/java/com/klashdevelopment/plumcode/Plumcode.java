package com.klashdevelopment.plumcode;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.klashdevelopment.plumcode.Numbers.evalMathExpression;

public class Plumcode {
    private static HashMap<String, String> variables = new HashMap<>();
    private static final Pattern pattern = Pattern.compile("\\$\\(([^)]+)\\)");
    private static boolean developmentMode = false,
    metaParsed = false,
    horizAlignGUI = false,
    vertcAlignGUI = false;

    public static void parse(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                for (String part : parts) {
                    if(part.startsWith(">>")) {
                        continue;
                    }
                    else if (part.startsWith("PRINT ")) {
                        parsePRINT(part);
                    } else if (part.startsWith("VAR ")) {
                        parseVAR(part);
                    } else if (part.startsWith("META ")) {
                        parseMETA(part);
                    } else if (part.startsWith("INPUT ")) {
                        parseINPUT(part);
                    } else if (part.startsWith("SET")) {
                        set(part.substring(4));
                    } else if (part.startsWith("GUI ")) {
                        parseGUI(part);
                    }
                }
            }
            reader.close();
            if(!metaParsed) {
                System.out.println("{Plumcode} We have not found a META tag in your file. Please add a META for your file if you plan to use this file in any real applications/programs.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void set(String property) {
        switch (property) {
            case "GuiCenterHorizontal":
                horizAlignGUI = !horizAlignGUI;
                System.out.println("{Config-SET} GuiCenterHorizontal has been set to " + horizAlignGUI);
                break;
            case "GuiCenterVertical":
                vertcAlignGUI = !vertcAlignGUI;
                System.out.println("{Config-SET} GuiCenterVertical has been set to " + vertcAlignGUI);
                break;
        }
    }

    public static void parseINPUT(String input) {
        String content = input.substring(6).trim();
        String varName = content.split(" ")[0];
        String prefix = content.substring(varName.length() + 2, content.length() - 1);
        Scanner in = new Scanner(System.in);
        System.out.print(prefix + " ");
        String s = in.nextLine();
        variables.put(varName, s);
    }

    public static String getString(String output) {
        if (output.startsWith("*")) {
            String varName = output.substring(1, output.length() - 1);
            if (variables.containsKey(varName)) {
                return (variables.get(varName));
            }
        } else if (output.startsWith("\"")) {
            Matcher matcher = pattern.matcher(output);
            while (matcher.find()) {
                String mathExpression = matcher.group(1);
                try {
                    if (variables.containsKey(mathExpression)) {
                        String value = variables.get(mathExpression);
                        output = output.replace("$(" + mathExpression + ")", value);
                    } else {
                        String value = String.valueOf(evalMathExpression(mathExpression));
                        output = output.replace("$(" + mathExpression + ")", value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return (output.substring(1, output.length() - 1));
        }
        return (String.valueOf(evalMathExpression(output)));
    }

    public static void parsePRINT(String input) {
        String output = input.substring(6).trim();
        if (output.startsWith("*")) {
            String varName = output.substring(1, output.length() - 1);
            if (variables.containsKey(varName)) {
                System.out.println((developmentMode ? "{Plumcode Debugger} " : "") + variables.get(varName));
            }
        } else if (output.startsWith("\"")) {
            Matcher matcher = pattern.matcher(output);
            while (matcher.find()) {
                String mathExpression = matcher.group(1);
                try{
                    if(variables.containsKey(mathExpression)) {
                        String value = variables.get(mathExpression);
                        output = output.replace("$(" + mathExpression + ")", value);
                    }else {
                        String value = String.valueOf(evalMathExpression(mathExpression));
                        output = output.replace("$(" + mathExpression + ")", value);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println((developmentMode ? "{Plumcode Debugger} " : "") + output.substring(1, output.length() - 1));
        } else {
            System.out.println((developmentMode ? "{Plumcode Debugger} " : "") + evalMathExpression(output));
        }
    }

    public static void parseGUI(String input) {
        String content = input.substring(4).trim();
        String[] inps = content.split("\"");
        String frameTitle = inps[1];
        String frameContent = inps[3];
        if(developmentMode) {
            System.out.println("{GUI} Gui \""+frameTitle+"\" is being created.");
            if(vertcAlignGUI) {
                System.out.println("{GUI} Center Align Vertical is enabled.");
            }
            if(horizAlignGUI) {
                System.out.println("{GUI} Center Align Horizontal is enabled.");
            }
        }

        Matcher matcher = pattern.matcher(frameContent);
        while (matcher.find()) {
            String mathExpression = matcher.group(1);
            try {
                if (variables.containsKey(mathExpression)) {
                    String value = variables.get(mathExpression);
                    frameContent = frameContent.replace("$(" + mathExpression + ")", value);
                } else {
                    String value = String.valueOf(evalMathExpression(mathExpression));
                    frameContent = frameContent.replace("$(" + mathExpression + ")", value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        JFrame frame = new JFrame(frameTitle);
        frame.setSize(800, 400);
        JLabel theLabel = new JLabel(frameContent);
        theLabel.setVerticalAlignment(vertcAlignGUI ? SwingConstants.CENTER : SwingConstants.NORTH);
        theLabel.setHorizontalAlignment(horizAlignGUI ? SwingConstants.CENTER : SwingConstants.LEFT);
        frame.add(theLabel, SwingConstants.CENTER);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void parseVAR(String input) {
        String[] varDeclaration = input.substring(4).trim().split("=");
        String varName = varDeclaration[0].trim();
        String varValue = varDeclaration[1].trim();
        if (varValue.startsWith("(") && varValue.endsWith(")")) {
            varValue = String.valueOf(evalMathExpression(varValue.substring(1, varValue.length() - 1)));
        } else if (varValue.startsWith("\"")) {
            Matcher matcher = pattern.matcher(varValue);
            while (matcher.find()) {
                String mathExpression = matcher.group(1);
                try{
                    if(variables.containsKey(mathExpression)) {
                        String value = variables.get(mathExpression);
                        varValue = varValue.replace("$(" + mathExpression + ")", value);
                    }else {
                        String value = String.valueOf(evalMathExpression(mathExpression));
                        varValue = varValue.replace("$(" + mathExpression + ")", value);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            varValue = varValue.substring(1, varValue.length() - 1);
        }
        variables.put(varName, varValue);
    }

    public static void parseMETA(String input) {
        String content = input.substring(5);
        String[] metas = content.split(" ");
        for(String meta : metas) {
            if(meta.equals("DEVELOPMENT")) {
                developmentMode = true;
            }else if(meta.equals("PRODUCTION")) {
                developmentMode = false;
            }
        }
        metaParsed = true;
    }

}
