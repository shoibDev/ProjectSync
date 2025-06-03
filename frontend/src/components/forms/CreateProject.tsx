import Modal from '../ui/Modal.tsx';
import '../ui/Modal.css'; // Using the new Modal.css instead of CreateProject.css

type ProjectModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

// This is now a wrapper around the more generic Modal component
export default function CreateProject({ isOpen, onClose }: ProjectModalProps) {
  return (
    <Modal 
      isOpen={isOpen} 
      onClose={onClose} 
      title="Create Project"
    >
      <p>Form goes here or any modal content you want.</p>
    </Modal>
  );
}
