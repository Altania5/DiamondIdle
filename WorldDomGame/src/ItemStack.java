public class ItemStack {
    public ResourceType type;
    public int quantity;

    public ItemStack(ResourceType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }
}