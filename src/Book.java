import java.math.BigDecimal;

public class Book 
{	
		   private String title;
		   private String author;
		   private BigDecimal price;
		   private int amount;

		   public String getTitle()
		   {
			   return this.title;
		   }
		   public String getAuthor()
		   {
			   return this.author;
		   }
		   public BigDecimal getPrice()
		   {
			   return this.price;
		   }
		   public int getAmount()
		   {
			   return this.amount;
		   }
		   
		   public void setTitle(String bookTitle)
		   {
			   this.title  = bookTitle;
		   }
		   public void setAuthor(String bookAuthor)
		   {
			   this.author  = bookAuthor;
		   }
		   public void setPrice(BigDecimal bookPrice)
		   {

			   this.price  = bookPrice;
		   }
		   public void setAmount(int amount)
		   {
			   this.amount = amount;
		   }
		}