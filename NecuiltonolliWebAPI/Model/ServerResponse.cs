namespace NecuiltonolliWebAPI.Model
{
    public class ServerResponse
    {
        public int statusCode { get; set; }
        public string message { get; set; }
        public Product product { get; set; }
        public List<Product> products { get; set; }
        public ItemMyList item { get; set; }
        public List<ItemMyList> items { get; set; }
    }
}
