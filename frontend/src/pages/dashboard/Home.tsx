import {useEffect} from "react";
import{usePageHeader} from "../../contexts/PageHeaderContext.tsx";


const Home = () => {
  const {setHeader} = usePageHeader();
  useEffect(() => {
    setHeader({
      title: "Home",
      actions: <></>
    });
  }, [setHeader]);

  return (
      <>

      </>
  )
}
export default Home;