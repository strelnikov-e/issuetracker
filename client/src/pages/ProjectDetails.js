import { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom"
import { useQueryClient } from "@tanstack/react-query";

import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';


export default function ProjectDetails() {
    const queryClient = useQueryClient();

    let initialFormState = {
        id: '',
        name: '',
        description: '',
        status: 'OPEN',
        startDate: ''
    };
    const [ project, setProject ] = useState(initialFormState);
    const navigate = useNavigate();
    const { id } = useParams();

    useEffect(() => {
        if (id !== 'new') {
            fetch(`/api/projects/${id}`)
            .then(response => response.json())
            .then(data => setProject(data))
        }
    }, [id, setProject]);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setProject({...project, [name]: value})
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        project['startDate'] = event.target.startDate.value;

        await fetch(`/api/projects${project.id ? `/${project.id}` : '' }`, {
            method: (project.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(project)
        });
        setProject(initialFormState);
        queryClient.invalidateQueries({ queryKey: ['projects'] });
        navigate('/projects');
    }

    const title = <h4 className="mb-4">{project.id ? 'Edit project' : 'Create project' }</h4>;

    return (
        <Form onSubmit={handleSubmit}> 
            {title}
            <Form.Group className="mb-4" controlId="formProjectName">
                <Form.Label>Name*</Form.Label>
                <Form.Control className="sm" name="name" type="text" required value={project.name || ""} onChange={handleChange}/>
            </Form.Group>

            <Form.Group className="mb-4" controlId="formProjectStartDate">
                <Form.Label>Start date</Form.Label>
                <Form.Control type="date" name="startDate" 
                value={project.startDate || (new Date().toISOString().slice(0,10))} onChange={handleChange}/>
            </Form.Group>
            <Form.Group className="mb-4" controlId="formProjectDescription"/>
                <Form.Label>Description</Form.Label>
                <Form.Control className="mb-4" as="textarea" rows={3} name="description" value={project.description || ""} onChange={handleChange} />
            <Form.Group >
                <Button variant="primary me-2" type="submit">Submit</Button>
                <Button variant="secondary" onClick={() => navigate(-1)}>Cancel</Button>
            </Form.Group>
        </Form>
    );
}
