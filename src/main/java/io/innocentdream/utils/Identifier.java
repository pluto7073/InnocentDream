package io.innocentdream.utils;

public class Identifier implements Comparable<Identifier> {

    public static final String DEFAULT_NAMESPACE = "innocentdream";
    public static final char NAMESPACE_SEPARATOR = ':';

    private final String namespace;
    private final String path;

    public Identifier(String id) {
        this(split(id));
    }

    private Identifier(String[] strings) {
        this(strings[0], strings[1]);
    }

    public Identifier(String namespace, String path) {
        verifyIdentifier(namespace, path);
        this.namespace = namespace;
        this.path = path;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    public String toString() {
        return namespace + ":" + path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Identifier id)) {
            return false;
        } else {
            return this.namespace.equals(id.namespace) && this.path.equals(id.path);
        }
    }

    @Override
    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    @Override
    public int compareTo(Identifier o) {
        int i = this.path.compareTo(o.path);
        if (i == 0) {
            i = this.namespace.compareTo(o.namespace);
        }
        return i;
    }

    public String toTranslationKey(String prefix) {
        return prefix + "." + toTranslationKey();
    }

    public String toTranslationKey() {
        return namespace + "." + path;
    }

    private static void verifyIdentifier(String namespace, String path) {
        for (int i = 0; i < namespace.length(); i++) {
            char c = namespace.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '.' || c == '_' || c == '-')) {
                throw new IllegalArgumentException("Non [a-z0-9._-] character in namespace: " + namespace);
            }
        }
        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '.' || c == '_' || c == '-' || c == '/')) {
                throw new IllegalArgumentException("Non [a-z0-9/._-] character in path: " + path);
            }
        }
    }

    protected static String[] split(String id) {
        String[] strings = new String[] { DEFAULT_NAMESPACE, id };
        int i = id.indexOf(NAMESPACE_SEPARATOR);
        if (i >= 0) {
            strings[1] = id.substring(i + 1);
            if (i >= 1) {
                strings[0] = id.substring(0, i);
            }
        }
        return strings;
    }

}
