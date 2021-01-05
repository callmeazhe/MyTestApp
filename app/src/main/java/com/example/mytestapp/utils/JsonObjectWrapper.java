package com.example.mytestapp.utils;

import net.sf.json.*;
import net.sf.json.util.JSONUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JsonObjectWrapper {
    private JSONObject mJsonObject;
    private Map properties;

    public JsonObjectWrapper(JSONObject object) {
        this.mJsonObject = object;
        this.properties = getJsonObjectPropertyMap();
    }

    public String toString() {
        if(properties == null) {
            return JSONNull.getInstance().toString();
        }

        try {
            Iterator keys = this.keys();
            StringBuffer sb = new StringBuffer("{");

            while(keys.hasNext()) {
                if (sb.length() > 1) {
                    sb.append(',');
                }

                Object o = keys.next();
                sb.append(JSONUtils.quote(o.toString()));
                sb.append(':');
                sb.append(valueToString(this.properties.get(o)));
            }

            sb.append('}');
            return sb.toString();
        } catch (Exception var4) {
            return null;
        }
    }

    public Iterator keys() {
        return this.keySet().iterator();
    }

    public Set keySet() {
        return Collections.unmodifiableSet(this.properties.keySet());
    }

    private Map getJsonObjectPropertyMap() {
        if(this.mJsonObject == null) {
            return null;
        }

        Map property = null;
        try {
            Field mapField = JSONObject.class.getDeclaredField("properties");
            mapField.setAccessible(true);
            Object mapObject = mapField.get(this.mJsonObject);
            if(mapObject != null && mapObject instanceof Map) {
                property = (Map) mapObject;
            }
        } catch (Exception e) {
            e.toString();
        }
        return property;
    }

    private String valueToString(Object value) {
        if (value != null && !JSONUtils.isNull(value)) {
            if (value instanceof JSONFunction) {
                return value.toString();
            } else if (value instanceof JSONString) {
                String o;
                try {
                    o = ((JSONString)value).toJSONString();
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return null;
                }

                if (o instanceof String) {
                    return o;
                } else {
                    return null;
                }
            } else if (value instanceof Number) {
                return numberToString((Number)value);
            } else if (value instanceof JSONObject){
                return new JsonObjectWrapper((JSONObject) value).toString();
            }else if (value instanceof JSONArray){
                StringBuffer buffer = new StringBuffer();
                for (int i = 0;i < ((JSONArray) value).size();i++){
                    buffer.append(new JsonObjectWrapper((JSONObject) value).toString());
                }
                return buffer.toString();
            }else {
                return !(value instanceof Boolean)  ? JSONUtils.quote(value.toString()) : value.toString();
            }
        } else {
            return "null";
        }
    }

    private String numberToString(Number n) {
        if (n == null) {
            return null;
        } else {
            JSONUtils.testValidity(n);
            String s = n.toString();
//            if (s.indexOf(46) > 0 && s.indexOf(101) < 0 && s.indexOf(69) < 0) {
//                while(s.endsWith("0")) {
//                    s = s.substring(0, s.length() - 1);
//                }
//
//                if (s.endsWith(".")) {
//                    s = s.substring(0, s.length() - 1);
//                }
//            }

            return s;
        }
    }
}


