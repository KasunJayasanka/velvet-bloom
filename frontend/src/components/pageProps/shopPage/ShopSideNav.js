import Category from "./shopBy/Category";
import Size from "./shopBy/Size";
import Price from "./shopBy/Price";

const ShopSideNav = ({ onCategorySelect, onSizeSelect, onPriceRangeSelect }) => {
  return (
    <div className="w-full flex flex-col gap-6">
      <Category onCategorySelect={onCategorySelect} />
      <Size onSizeSelect={onSizeSelect} />
      <Price onPriceRangeSelect={onPriceRangeSelect} />
    </div>
  );
};

export default ShopSideNav;