using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using NecuiltonolliWebAPI.Model;
using System.Data;

namespace NecuiltonolliWebAPI.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class NecuiltonolliController : Controller
    {
        private readonly IConfiguration configuration;

        public NecuiltonolliController(IConfiguration _configuration)
        {
            configuration = _configuration;
        }

        //Getting lists
        [HttpGet]
        [Route("GetAllProducts")]
        public ServerResponse getAllProducts()
        {
            ServerResponse response = new ServerResponse();

            try
            {
                SqlConnection connection = new SqlConnection(configuration.GetConnectionString("Necuiltonolli"));
                string query = "SELECT * FROM dbo.Library";

                connection.Open();
                SqlCommand command = new SqlCommand(query, connection);
                SqlDataAdapter adapter = new SqlDataAdapter(command);
                DataTable dt = new DataTable();
                adapter.Fill(dt);

                List<Product> products = new List<Product>();
                if (dt.Rows.Count > 0)
                {
                    foreach (DataRow row in dt.Rows)
                    {
                        Product product = new Product();
                        product.id = (int)row["Id"];
                        product.name = row["Name"].ToString();
                        product.type = row["Type"].ToString();
                        product.description = row["Descrip"].ToString();
                        products.Add(product);
                    }
                }

                response.products = products;
                if (products.Count > 0)
                {
                    response.statusCode = 200;
                    response.message = "Library data obtained from server!";
                    response.product = null;
                    response.item = null;
                    response.items = null;
                }
                else
                {
                    response.statusCode = 100;
                    response.message = "Library data not found!";
                    response.product = null;
                    response.item = null;
                    response.items = null;
                }

                connection.Close();
            }
            catch(SqlException ex)
            {
                response.statusCode = ex.ErrorCode;
                response.message = ex.Message;
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;
            }

            return response;
        }

        [HttpGet]
        [Route("GetAllMyList")]
        public ServerResponse getAllMyList()
        {
            ServerResponse response = new ServerResponse();

            try
            {
                SqlConnection connection = new SqlConnection(configuration.GetConnectionString("Necuiltonolli"));
                string query = "SELECT * FROM dbo.MyList";

                connection.Open();
                SqlCommand command = new SqlCommand(query, connection);
                SqlDataAdapter adapter = new SqlDataAdapter(command);
                DataTable dt = new DataTable();
                adapter.Fill(dt);

                List<ItemMyList> myList = new List<ItemMyList>();
                if(dt.Rows.Count > 0)
                {
                    foreach(DataRow row in dt.Rows)
                    {
                        ItemMyList item = new ItemMyList();
                        item.id = (int)row["Id"];
                        item.productId = (int)row["ProductId"];
                        item.status = row["Status"].ToString();
                        myList.Add(item);
                    }
                }

                response.items = myList;
                if(myList.Count > 0)
                {
                    response.statusCode = 200;
                    response.message = "MyList data obtained from database!";
                    response.product = null;
                    response.products = null;
                    response.item = null;
                }
                else
                {
                    response.statusCode = 100;
                    response.message = "MyList Empty!";
                    response.product = null;
                    response.products = null;
                    response.item = null;
                }

                connection.Close();
            }
            catch (SqlException ex)
            {
                response.statusCode = ex.ErrorCode;
                response.message = ex.Message;
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;
            }

            return response;
        }

        //Add items
        [HttpPost]
        [Route("AddNewProduct")]
        public ServerResponse addNewProduct(Product product)
        {
            ServerResponse response = new ServerResponse();

            try
            {
                SqlConnection connection = new SqlConnection(configuration.GetConnectionString("Necuiltonolli"));
                string query = "INSERT INTO dbo.Library VALUES (@name, @type, @descrip)";

                connection.Open();
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@name", product.name);
                command.Parameters.AddWithValue("@type", product.type);
                command.Parameters.AddWithValue("@descrip", product.description);

                int r = command.ExecuteNonQuery();
                if (r > 0)
                {
                    response.statusCode = 200;
                    response.message = "New product added to Library!";
                    response.product = product;
                }
                else
                {
                    response.statusCode = 100;
                    response.message = "Error adding new product to Library!";
                    response.product = null;
                }
                response.products = null;
                response.item = null;
                response.items = null;

                connection.Close();
            }
            catch (SqlException ex)
            {
                response.statusCode = ex.ErrorCode;
                response.message = ex.Message;
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;
            }

            return response;
        }

        [HttpPost]
        [Route("AddItemToMyList/{productId}")]
        public ServerResponse addItemToMyList(int productId)
        {
            ServerResponse response = new ServerResponse();

            try
            {
                SqlConnection connection = new SqlConnection(configuration.GetConnectionString("Necuiltonolli"));
                string query = "INSERT INTO dbo.MyList VALUES (@productId, 'NS')";

                connection.Open();
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@productId", productId);

                int r = command.ExecuteNonQuery();
                if(r > 0)
                {
                    ItemMyList item = new ItemMyList();
                    item.productId = productId;
                    item.status = "NS";

                    response.statusCode = 200;
                    response.message = "Item added to MyList!";
                    response.item = item;
                }
                else
                {
                    response.statusCode = 100;
                    response.message = "Failed to add item to MyList!";
                    response.item = null;
                }
                response.product = null;
                response.products = null;
                response.items = null;

                connection.Close();
            }
            catch (SqlException ex)
            {
                response.statusCode = ex.ErrorCode;
                response.message = ex.Message;
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;
            }

            return response;
        }

        //update
        [HttpPut]
        [Route("UpdateProduct")]
        public ServerResponse updateProduct(Product product)
        {
            ServerResponse response = new ServerResponse();

            try
            {
                SqlConnection connection = new SqlConnection(configuration.GetConnectionString("Necuiltonolli"));
                string query = "UPDATE dbo.Library SET Name = @name, Type = @type, Descrip = @descrip " +
                               "WHERE Id = @id";

                connection.Open();
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@name", product.name);
                command.Parameters.AddWithValue("@type", product.type);
                command.Parameters.AddWithValue("@descrip", product.description);
                command.Parameters.AddWithValue("@id", product.id);

                int r = command.ExecuteNonQuery();
                if (r > 0)
                {
                    response.statusCode = 200;
                    response.message = "Product updated in Library!";
                    response.product = product;
                }
                else
                {
                    response.statusCode = 100;
                    response.message = "Failed to update product in Lbrary!";
                    response.product = null;
                }
                response.products = null;
                response.item = null;
                response.items = null;

                connection.Close();
            }
            catch (SqlException ex)
            {
                response.statusCode = ex.ErrorCode;
                response.message = ex.Message;
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;
            }

            return response;
        }

        [HttpPut]
        [Route("ChangeMyListItemStatus")]
        public ServerResponse updateItemStatus(ItemMyList item)
        {
            ServerResponse response = new ServerResponse();

            try
            {
                SqlConnection connection = new SqlConnection(configuration.GetConnectionString("Necuiltonolli"));
                string query = "UPDATE dbo.MyList SET Status = @status " +
                               "WHERE Id = @id OR ProductId = @productId";

                connection.Open();
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@status", item.status);
                command.Parameters.AddWithValue("@id", item.id);
                command.Parameters.AddWithValue("@productId", item.productId);

                int r = command.ExecuteNonQuery();
                if (r > 0)
                {
                    response.statusCode = 200;
                    response.message = "MyList item status updated!";
                    response.item = item;
                }
                else
                {
                    response.statusCode = 100;
                    response.message = "Failed to update item status in MyList!";
                    response.item = null;
                }
                response.product = null;
                response.products = null;
                response.items = null;

                connection.Close();
            }
            catch (SqlException ex)
            {
                response.statusCode = ex.ErrorCode;
                response.message = ex.Message;
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;
            }

            return response;
        }

        //remove
        [HttpDelete]
        [Route("DeleteProduct/{productId}")]
        public ServerResponse deleteProduct(int productId)
        {
            ServerResponse response = new ServerResponse();

            try
            {
                SqlConnection connection = new SqlConnection(configuration.GetConnectionString("Necuiltonolli"));
                string query = "DELETE FROM dbo.Library WHERE Id = @id";

                connection.Open();
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@id", productId);

                int r = command.ExecuteNonQuery();
                if (r > 0)
                {
                    response.statusCode = 200;
                    response.message = "Product deleted from Library!";
                }
                else
                {
                    response.statusCode = 100;
                    response.message = "Failed to delete profuct from Lobrary!";
                }
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;

                connection.Close();
            }
            catch (SqlException ex)
            {
                response.statusCode = ex.ErrorCode;
                response.message = ex.Message;
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;
            }

            return response;
        }

        [HttpDelete]
        [Route("DeleteItemMyList/{itemId}")]
        public ServerResponse deleteItem(int itemId)
        {
            ServerResponse response = new ServerResponse();

            try
            {
                SqlConnection connection = new SqlConnection(configuration.GetConnectionString("Necuiltonolli"));
                string query = "DELETE FROM dbo.MyList WHERE Id = @id";

                connection.Open();
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@id", itemId);

                int r = command.ExecuteNonQuery();
                if (r > 0)
                {
                    response.statusCode = 200;
                    response.message = "Item deleted from MyList!";
                }
                else
                {
                    response.statusCode = 100;
                    response.message = "Failed to delete item from MyList!";
                }
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;

                connection.Close();
            }
            catch (SqlException ex)
            {
                response.statusCode = ex.ErrorCode;
                response.message = ex.Message;
                response.product = null;
                response.products = null;
                response.item = null;
                response.items = null;
            }

            return response;
        }
    }
}