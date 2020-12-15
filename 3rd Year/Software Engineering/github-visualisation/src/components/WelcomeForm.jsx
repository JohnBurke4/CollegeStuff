import React, { Component } from "react";
import Form from "react-bootstrap/Form"
import Button from "react-bootstrap/Button"

class WelcomeForm extends Component {
    render() {
        return (
            
            <Form className="mx-auto my-auto">
                <Form.Group controlId="formAuthCode">
                    <Form.Label>Authentification Token</Form.Label>
                    <Form.Control placeholder="Enter Auth Token" />
                        <Form.Text className="text-muted">
                            This isn't saved anywhere.
                        </Form.Text>
                </Form.Group>

                <Form.Group controlId="formRepoOwner">
                    <Form.Label>Repository Owner</Form.Label>
                    <Form.Control placeholder="Enter Repo Owner" />
                </Form.Group>
                <Form.Group controlId="formRepoName">
                    <Form.Label>Repository Name</Form.Label>
                    <Form.Control placeholder="Enter Repo Name" />
                </Form.Group>
                <Button variant="primary" type="submit">
                    Submit
                </Button>
            </Form>
        );
    }
}

export default WelcomeForm;