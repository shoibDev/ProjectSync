import { Dialog, Transition } from '@headlessui/react';
import { Fragment, ReactNode } from 'react';
import './Modal.css'; // Updated CSS import

type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  children?: ReactNode;
};

export default function Modal({ isOpen, onClose, title, children }: ModalProps) {
  return (
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="modal-container" onClose={onClose}>
          <div className="modal-backdrop">
            <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                leave="ease-in duration-500"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
            >
              <div className="modal-overlay" />
            </Transition.Child>

            <Transition.Child
                as={Fragment}
                enter="transition duration-500 ease-out"
                enterFrom="opacity-0 translate-y-4 scale-95"
                enterTo="opacity-100 translate-y-0 scale-100"
                leave="transition duration-400 ease-in"
                leaveFrom="opacity-100 translate-y-0 scale-100"
                leaveTo="opacity-0 translate-y-4 scale-95"
            >
              <div className="modal-panel">
                {title && <Dialog.Title className="modal-title">{title}</Dialog.Title>}
                <div className="modal-body">
                  {children || <p>Form goes here or any modal content you want.</p>}
                </div>
                <div className="modal-footer">
                  <button className="primary-btn" onClick={onClose}>Close</button>
                </div>
              </div>
            </Transition.Child>
          </div>
        </Dialog>
      </Transition>
  );
}