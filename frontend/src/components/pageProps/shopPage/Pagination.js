import React, { useState, useEffect, useMemo } from "react";
import ReactPaginate from "react-paginate";
import Product from "../../home/Products/Product";

function Items({ currentItems }) {
  return (
    <>
      {currentItems &&
        currentItems.map((item) => (
          <div key={item.id} className="w-full">
            <Product
              id={item.id}
              img={item.mainImgUrl}
              productName={item.productName}
              price={item.unitPrice}
              description={item.description}
              brand={item.brand}
              discount={item.discount}
              Size={item.variety.map((v) => v.size).join(", ")}
              color={item.variety
                .flatMap((v) => v.colors.map((c) => c.color))
                .join(", ")}
              badge={`${item.discount}% OFF`}
              productCount={item.productCount}
              categories={item.categories}
              varieties={item.variety}
              reviews={item.reviews}
              lowStockCount={item.lowStockCount}
              imageGallery={item.imageGallery}
              createdAt={item.createdAt}
              updatedAt={item.updatedAt}
            />
          </div>
        ))}
    </>
  );
}

const Pagination = ({ itemsPerPage, paginationItems }) => {
  const [itemOffset, setItemOffset] = useState(0);
  const [itemStart, setItemStart] = useState(1);

  // Memoize the items array
  const items = useMemo(() => paginationItems || [], [paginationItems]);

  const endOffset = itemOffset + itemsPerPage;
  const currentItems = items.slice(itemOffset, endOffset);
  const pageCount = Math.ceil(items.length / itemsPerPage);

  useEffect(() => {
    // Reset pagination when items change
    setItemOffset(0);
    setItemStart(1);
  }, [items]); // Memoized items won't cause unnecessary re-renders

  const handlePageClick = (event) => {
    const newOffset = (event.selected * itemsPerPage) % items.length;
    setItemOffset(newOffset);
    setItemStart(newOffset + 1);
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
          Products from {itemStart} to{" "}
          {Math.min(itemStart + itemsPerPage - 1, items.length)} of {items.length}
        </p>
      </div>
    </div>
  );
};

export default Pagination;
