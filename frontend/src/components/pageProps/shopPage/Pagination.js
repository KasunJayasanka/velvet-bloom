import React, { useState } from "react";
import ReactPaginate from "react-paginate";
import Product from "../../home/Products/Product";
import { paginationItems } from "../../../constants";

const items = paginationItems;
function Items({ currentItems }) {
  return (
    <>
      {currentItems &&
        currentItems.map((item) => (
          <div key={item._id.$oid} className="w-full">
            <Product
              // Basic Product Details
              _id={item._id.$oid}
              img={item.mainImgUrl}
              productName={item.productName}
              price={item.unitPrice}
              
              // Comprehensive Product Information
              description={item.description}
              brand={item.brand}
              discount={item.discount}
              
              // Size and Color Handling
              Size={item.variety.map(v => v.size).join(', ')}
              color={item.variety
                .flatMap(v => v.colors.map(c => c.color))
                .join(', ')
              }
              
              // Badge and Stock Information
              badge={`${item.discount}% OFF`}
              productCount={item.productCount}
              lowStockCount={item.lowStockCount}
              
              // Image Gallery
              imageGallery={item.imageGallery}
              
              // Categories
              categories={item.categories}
              
              // Detailed Variety Information
              varieties={item.variety.map(variety => ({
                size: variety.size,
                colors: variety.colors.map(color => ({
                  color: color.color,
                  count: color.count
                }))
              }))}
              
              // Reviews
              reviews={item.reviews.map(review => ({
                customerName: review.fName,
                description: review.description
              }))}
              
              // Timestamps
              createdAt={item.createdAt}
              updatedAt={item.updatedAt}
            />
          </div>
        ))}
    </>
  );
}


const Pagination = ({ itemsPerPage }) => {
  // Here we use item offsets; we could also use page offsets
  // following the API or data you're working with.
  const [itemOffset, setItemOffset] = useState(0);
  const [itemStart, setItemStart] = useState(1);

  // Simulate fetching items from another resources.
  // (This could be items from props; or items loaded in a local state
  // from an API endpoint with useEffect and useState)
  const endOffset = itemOffset + itemsPerPage;
  //   console.log(`Loading items from ${itemOffset} to ${endOffset}`);
  const currentItems = items.slice(itemOffset, endOffset);
  const pageCount = Math.ceil(items.length / itemsPerPage);

  // Invoke when user click to request another page.
  const handlePageClick = (event) => {
    const newOffset = (event.selected * itemsPerPage) % items.length;
    setItemOffset(newOffset);
    // console.log(
    //   `User requested page number ${event.selected}, which is offset ${newOffset},`
    // );
    setItemStart(newOffset);
  };

  return (
    <div>
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-10 mdl:gap-4 lg:gap-10">
        <Items currentItems={currentItems} />
      </div>
      <div className="flex flex-col mdl:flex-row justify-center mdl:justify-between items-center">
        <ReactPaginate
          nextLabel=""
          onPageChange={handlePageClick}
          pageRangeDisplayed={3}
          marginPagesDisplayed={2}
          pageCount={pageCount}
          previousLabel=""
          pageLinkClassName="w-9 h-9 border-[1px] border-lightColor hover:border-gray-500 duration-300 flex justify-center items-center"
          pageClassName="mr-6"
          containerClassName="flex text-base font-semibold font-titleFont py-10"
          activeClassName="bg-black text-white"
        />

        <p className="text-base font-normal text-lightText">
          Products from {itemStart === 0 ? 1 : itemStart} to {endOffset} of{" "}
          {items.length}
        </p>
      </div>
    </div>
  );
};

export default Pagination;
