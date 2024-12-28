public class ItemStack {
    public Item item; // Now stores an Item object
    public int quantity;

    public ItemStack(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    //add getter for the item.
    public Item getItem() {
        return this.item;
    }
}
