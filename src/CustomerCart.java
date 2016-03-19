import java.util.Vector;

public class CustomerCart 
{
	private static final int OK = 0;
	
	private static final int NO_ITEMS_IN_CART = 10;
	private static final int NO_BOOK_FROM_THAT_AUTHOR_OR_TITLE = 12;
	
	private Vector<Book> vectorOfBooks;
	
	public CustomerCart()
	{
		this.vectorOfBooks = new Vector<Book>();
	}
	public void AddToCart(Book book, int amount)
	{
		int index =-1;
		boolean bookAlreadyExists = false;
		Book vecBooks = new Book();
		//Check if the book already are in the vector
		for(int i=0; i< this.vectorOfBooks.size();++i)
		{
			vecBooks = this.vectorOfBooks.get(i);
			if(vecBooks.getTitle().matches(book.getTitle()) && vecBooks.getAuthor().matches(book.getAuthor()))
			{
				System.out.println("Book exist in the vector");
				bookAlreadyExists = true;
				index = i;
				break;
			}
		}
		if(bookAlreadyExists)
		{
			
			//The book already exist in the vector add the amount of the book");
			Book tmpBook = this.vectorOfBooks.get(index);
			Book tmp = new Book();
			if((tmpBook.getAmount() + amount) <= 0)
			{
				EraseFromCart(book, tmpBook.getAmount());
			}
			else
			{
				tmp.setTitle(tmpBook.getTitle());
				tmp.setAuthor(tmpBook.getAuthor());
				tmp.setPrice(tmpBook.getPrice());
				tmp.setAmount(tmpBook.getAmount() + amount);	
				this.vectorOfBooks.set(index, tmp);
			}
		}
		else
		{
			//New book that aren't on the list
			Book tmp = new Book();
			tmp.setTitle(book.getTitle());
			tmp.setAuthor(book.getAuthor());
			tmp.setPrice(book.getPrice());
			tmp.setAmount(amount);
			this.vectorOfBooks.add(tmp);
		}
	}
	
	public int EraseFromCart(Book book, int amount)
	{
		if(this.vectorOfBooks.size() == 0)
		{
			return NO_ITEMS_IN_CART;
		}
		else 
		{
			int index =-1;
			boolean bookInCart = false;
			Book vecBooks = new Book();
			for(int i=0; i< this.vectorOfBooks.size();++i)
			{
				vecBooks = this.vectorOfBooks.get(i);
				if(vecBooks.getTitle().matches(book.getTitle()) && vecBooks.getAuthor().matches(book.getAuthor()))
				{
					bookInCart = true;
					index = i;
					break;
				}
			}
			if(bookInCart)
			{
				boolean removedBook = this.vectorOfBooks.remove(index) != null;
				if(removedBook == true)
				{
					return OK;
				}
			}
			return NO_BOOK_FROM_THAT_AUTHOR_OR_TITLE;
		}
	}
	
	public Book[] BooksInCart()
	{
		if(this.vectorOfBooks.size() == 0)
		{
			return null;
		}
		else
		{
			int size = this.vectorOfBooks.size();
			Book[] book = new Book[size];
			int count = 0;
			while(count < size)
			{
				book[count] = this.vectorOfBooks.get(count);
				count++;
			}
			return book;
		}
	}
}
