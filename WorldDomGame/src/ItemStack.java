public class ItemStack {
    public Item item;
    public int quantity;

    public ItemStack(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return this.item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}