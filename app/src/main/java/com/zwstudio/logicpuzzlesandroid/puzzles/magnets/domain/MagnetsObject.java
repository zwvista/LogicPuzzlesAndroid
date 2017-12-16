package com.zwstudio.logicpuzzlesandroid.puzzles.magnets.domain;

public enum MagnetsObject {
    Empty {
        public boolean isEmpty() {return true;}
        public boolean isPole() {return false;}
    },
    Marker {
        public boolean isEmpty() {return true;}
        public boolean isPole() {return false;}
    },
    Negative {
        public boolean isEmpty() {return false;}
        public boolean isPole() {return true;}
    },
    Positive {
        public boolean isEmpty() {return false;}
        public boolean isPole() {return true;}
    };

    public abstract boolean isEmpty();
    public abstract boolean isPole();
}
