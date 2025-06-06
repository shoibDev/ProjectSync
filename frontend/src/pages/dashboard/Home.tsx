import {useState, useEffect} from 'react';
import './Home.css'
import {ProjectTable} from "../../components/tables/ProjectTable.tsx";

import { useHeader } from "../../contexts/HeaderContext.tsx"
import { getAllProjects} from "../../api/projects.ts";
import { Project } from "../../types/application.tsx";


// This will have the project tables in here.
const Home = () => {
  const {setTitle} = useHeader();
  const [projects, setProjects] = useState<Project[]> ([]);

  useEffect(() => {
    setTitle("Dashboard");
    const fetchProjects = async () => {
      try {
        const projectsData = await getAllProjects();
        setProjects(projectsData);
      } catch (error) {
        console.error("Error fetching projects:", error);
      }
    };
    fetchProjects();
  }, []);


  return (
      <>
        <ProjectTable
            projects={projects}
        />
      </>
  )
}
export default Home;