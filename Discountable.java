//7th CLASS created
//I realized i wanted to make items in the 'store' discountable, so here I am able to do that.
//Interfaces defines a behaviour, what the object can do, regardless of what it is. Not every item will be on sale, but those that are, must have a way to apply it.
public interface Discountable {
    void Apply_Discount(double disc_percentage);
}