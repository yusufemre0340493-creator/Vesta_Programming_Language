package com.mycompany.vestaide;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Interpreter {

    private final Map<String, Object> variables = new HashMap<>();
    private Consumer<String> output = System.out::println;

    public Interpreter() {
    }

    public void setOutput(Consumer<String> output) {
        this.output = output;
    }

    // === ANA ÇALIŞTIRICI ===
    public void execute(String line) {
        if (line.isEmpty())
            return;

        // printIn(...)
        if (line.startsWith("printIn")) {
            handlePrint(line);
            return;
        }

        // if tek satır
        if (line.startsWith("if ")) {
            handleIf(line);
            return;
        }

        // değişken atama
        if (line.contains("=")) {
            handleAssign(line);
        }
    }

    // === PRINT ===
    private void handlePrint(String line) {
        String inside = line.substring(
                line.indexOf("(") + 1,
                line.lastIndexOf(")")).trim();

        Object value = eval(inside);
        output.accept(String.valueOf(value));
    }

    // === IF / ELSE (TEK SATIR) ===
    private void handleIf(String line) {
        // if a == 5 printIn("ok") else printIn("no")

        String[] parts = line.split("else");
        String ifPart = parts[0].trim();
        String elsePart = parts.length > 1 ? parts[1].trim() : null;

        String condition = ifPart.substring(2, ifPart.indexOf("printIn")).trim();
        String ifAction = ifPart.substring(ifPart.indexOf("printIn")).trim();

        boolean result = evalCondition(condition);

        if (result) {
            execute(ifAction);
        } else if (elsePart != null) {
            execute(elsePart);
        }
    }

    private boolean evalCondition(String cond) {
        // a == 5
        String[] parts = cond.split("==");
        Object left = eval(parts[0].trim());
        Object right = eval(parts[1].trim());
        return String.valueOf(left).equals(String.valueOf(right));
    }

    // === ATAMA ===
    private void handleAssign(String line) {
        String[] parts = line.split("=", 2);
        String name = parts[0].trim();
        String expr = parts[1].trim();

        Object value = eval(expr);
        variables.put(name, value);
    }

    // === İFADE DEĞERLENDİRME ===
    private Object eval(String expr) {
        // string
        if (expr.startsWith("\"") && expr.endsWith("\"")) {
            return expr.substring(1, expr.length() - 1);
        }

        // toplama (string + veya int +)
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+");
            StringBuilder sb = new StringBuilder();
            boolean allInt = true;
            int sum = 0;

            for (String p : parts) {
                Object v = eval(p.trim());
                if (v instanceof Integer) {
                    sum += (Integer) v;
                } else {
                    allInt = false;
                    sb.append(v);
                }
            }
            return allInt ? sum : sb.toString();
        }

        // sayı
        if (expr.matches("-?\\d+")) {
            return Integer.parseInt(expr);
        }

        // değişken
        if (variables.containsKey(expr)) {
            return variables.get(expr);
        }

        return expr;
    }
}
