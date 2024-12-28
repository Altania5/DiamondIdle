// Resource class extending Item

public class Resource extends Item {
    private ResourceType resourceType;



    public Resource(ResourceType resourceType) {
        super(resourceType.name(), "WorldDomGame/res/resources/" + resourceType.name().toLowerCase() + ".png"); // Consistent image naming
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    //Resource specific methods if needed
}