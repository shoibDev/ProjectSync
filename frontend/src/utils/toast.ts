import {toast} from "react-toastify";

export const showError = (message: string) =>
    toast.error(message, {
      position: "top-right",
      autoClose: 4000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      theme: "dark",
    });

export const showSuccess = (message: string) =>
    toast.success(message, {
      position: "top-right",
      autoClose: 3000,
      theme: "dark",
    });