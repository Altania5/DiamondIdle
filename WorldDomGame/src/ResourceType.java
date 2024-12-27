public enum ResourceType {
    ORE(2),
    GAS(3),
    CRYSTAL(2),
    ENERGY(3),
    GEM(1),
    STONE(6);

    private final int rarity;

    ResourceType(int rarity) {
        this.rarity = rarity;
    }

    public int getRarity() {
        return rarity;
    }
}
