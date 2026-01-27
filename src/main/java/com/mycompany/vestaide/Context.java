package com.mycompany.vestaide;

import java.util.HashMap;

public class Context {

    private final HashMap<String, Object> variables = new HashMap<>();

    public void set(String name, Object value) {
        variables.put(name, value);
    }

    public Object get(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Undefined variable: " + name);
        }
        return variables.get(name);
    }

    public boolean exists(String name) {
        return variables.containsKey(name);
    }
}
