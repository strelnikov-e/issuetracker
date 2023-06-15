import { useParams, useNavigate, useLocation } from "react-router-dom"
import { useEffect, useState, useContext } from "react";
import axios from 'axios'
import { useQueryClient } from "@tanstack/react-query";

import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

import { ProjectContext } from "../App";

export default function IssueDetails() {
    const { currentProject } = useContext(ProjectContext);
    const url = `/api/issues?projectId=${currentProject.id}`;
    const queryClient = useQueryClient();
    const { id } = useParams();
    const { state } = useLocation();
    console.log(state)
    const { projectId } = state? state : 0;

    let initialFormState = {
        id: '',
        name: '',
        description: '',
        project: {id: projectId},
        status: 'TODO',
        startDate: ''
    };

    const [ draftIssue, setDraftIssue ] = useState(initialFormState);
    const navigate = useNavigate();
    

    useEffect(() => {
        if (id !== 'new') {
            fetch(`/api/issues/${id}`)
            .then(response => response.json())
            .then(data => setDraftIssue(data))
        }
    }, [id]);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setDraftIssue({...draftIssue, [name]: value})
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        draftIssue['startDate'] = event.target.startDate.value;
        if (!draftIssue.id) {
            await axios.post(`/api/issues`, draftIssue);
        } else {
            await axios.put(`/api/issues/${draftIssue.id}`, draftIssue);
        }
        setDraftIssue(initialFormState);
        queryClient.invalidateQueries({ queryKey: [url] });
        navigate(-1);
    }

    const title = <h4 className="mb-4">{draftIssue.id ? 'Edit issue' : 'Create issue' }</h4>;

    return (
        <Form onSubmit={handleSubmit}>
            {title}
            <Form.Group className="mb-4" controlId="formIssueName">
                <Form.Label>Name*</Form.Label>
                <Form.Control className="sm" type="text" name="name" required value={draftIssue.name || "" } onChange={handleChange} />
            </Form.Group>

            <Form.Group className="mb-4" controlId="formIssueStartDate">
                <Form.Label>Start date</Form.Label>
                <Form.Control type="date" name="startDate" value={draftIssue.startDate || (new Date().toISOString().slice(0,10))} 
                onChange={handleChange} />
            </Form.Group>
            <Form.Group className="mb-4" controlId="formIssueDescription">
                <Form.Label>Description</Form.Label>
                <Form.Control as="textarea" rows={3} name="description" value={draftIssue.description || ""} onChange={handleChange} />
            </Form.Group>
            <Form.Select className="mb-4" aria-label="selectIssueStatus" name="status" value={draftIssue.status || "TODO"} onChange={handleChange}>
                <option value="TODO">To do</option>
                <option value="INPROGRESS">In progress</option>
                <option value="INREVIEW">In review</option>
                <option value="DONE">Done</option>
            </Form.Select>
            <Form.Group >
                <Button variant="primary me-2" type="submit">Submit</Button>
                <Button variant="secondary" onClick={() => navigate(-1)}>Cancel</Button>
            </Form.Group>
        </Form>
    );
}
