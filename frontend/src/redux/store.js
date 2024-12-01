import { configureStore } from "@reduxjs/toolkit";
import {
  persistStore,
  persistReducer,
  FLUSH,
  REHYDRATE,
  PAUSE,
  PERSIST,
  PURGE,
  REGISTER,
} from "redux-persist";
import storage from "redux-persist/lib/storage";
import velvetReducer from "./velvetSlice";
import checkoutReducer from './checkoutSlice';

const persistConfig = {
  key: "root",
  version: 1,
  storage,
};

const persistedReducer = persistReducer(persistConfig, velvetReducer);

export const store = configureStore({
  reducer: { velvetReducer: persistedReducer },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
      },
    }),
    checkout: checkoutReducer,
});

export let persistor = persistStore(store);
