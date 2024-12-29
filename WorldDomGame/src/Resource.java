public class Resource extends Item {
    private ResourceType resourceType;

    public Resource(String name, String imagePath, ResourceType resourceType) {
        super(name, imagePath);
        this.resourceType = resourceType;
    }

    public Resource(ResourceType resourceType) {
        super("Resource", ""); // You might want to provide default name and image path
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}