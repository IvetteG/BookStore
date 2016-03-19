import java.math.BigDecimal;
import java.util.Vector;

public class BookStoreList implements BookList
	{
		private static final int OK = 0;
		private static final int NOT_IN_STOCK = 1;
		private static final int DOES_NOT_EXIST = 2;
		private Vector<Book> vectorOfBooks;
		
		public BookStoreList()
		{
			this.vectorOfBooks = new Vector<Book>();
		}
		
		@Override
		public Book[] list(String searchString) 
		{
			int i =0;
			int searchedBooks=0;
			
			while(i < this.vectorOfBooks.size())
			{
				Book tmpBook = this.vectorOfBooks.get(i);
				String author = tmpBook.getAuthor().toLowerCase();
				String title  = tmpBook.getTitle().toLowerCase();
				if(author.matches(searchString) == true || 
					title.matches(searchString) == true)
				{
					searchedBooks++;
				}
				i++;
			}
			int count = 0;
			int searched = 0;
			Book[] list = new Book[searchedBooks];
			while(count < this.vectorOfBooks.size())
			{
				String author = this.vectorOfBooks.get(count).getAuthor().toLowerCase();
				String title = this.vectorOfBooks.get(count).getTitle().toLowerCase();
				if(author.matches(searchString) == true ||
					title.matches(searchString)	)
				{
					list[searched] = this.vectorOfBooks.get(count);
					searched++;
				}
				count++;
			}
			
			if(searchedBooks==0)
			{
				return null;
			}
			return list;
		}

		@Override
		public boolean add(Book book, int amount) 
		{
			int index =-1;
			boolean bookAlreadyExists = false;
			Book vecBooks = new Book();
			for(int i=0; i< this.vectorOfBooks.size();++i)
			{
				vecBooks = this.vectorOfBooks.get(i);
				if(vecBooks.getTitle().matches(book.getTitle()) && vecBooks.getAuthor().matches(book.getAuthor()))
				{
					bookAlreadyExists = true;
					index = i;
					break;
				}
			}
			if(bookAlreadyExists)
			{
				Book tmpBook = this.vectorOfBooks.get(index);
				Book tmp = new Book();
				tmp.setTitle(tmpBook.getTitle());
				tmp.setAuthor(tmpBook.getAuthor());
				tmp.setPrice(tmpBook.getPrice());
				tmp.setAmount(tmpBook.getAmount() + amount);	
				this.vectorOfBooks.set(index, tmp);
				return true;
			}
			else
			{
				Book tmp = new Book();
				tmp.setTitle(book.getTitle());
				tmp.setAuthor(book.getAuthor());
				tmp.setPrice(book.getPrice());
				tmp.setAmount(amount);
				this.vectorOfBooks.add(tmp);
				return true;
			}
		}
		
		@Override
		public int[] buy(Book... books) 
		{
			if(books == null)
			{
				return null; //cart is empty
			}
			
			int[] buyBooks = new int[books.length];
			for(int i = 0; i < books.length; ++i)
			{
				int index =-1;
				boolean bookThatExistsInTheStore = false;
				Book vecBooks = new Book();
				for(int j=0; j< this.vectorOfBooks.size();++j)
				{
					vecBooks = this.vectorOfBooks.get(j);
					if(vecBooks.getTitle().matches(books[i].getTitle()) && vecBooks.getAuthor().matches(books[i].getAuthor()))
					{
						bookThatExistsInTheStore = true;
						index = j;
						break;
					}
				}
				
				
				if(bookThatExistsInTheStore)
				{
					Book tmp = new Book();
					tmp = this.vectorOfBooks.get(index);
					if(books[i].getAmount() <= tmp.getAmount())
					{
						buyBooks[i] = OK;
					}
					else if(books[i].getAmount() > tmp.getAmount())
					{
						buyBooks[i] = NOT_IN_STOCK;
					}
				}
				if(!bookThatExistsInTheStore)
				{
					buyBooks[i] = DOES_NOT_EXIST;
				}
			}
			return buyBooks;
		}
		
		
		@Override
		public Book[] ListAllBooks()
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

