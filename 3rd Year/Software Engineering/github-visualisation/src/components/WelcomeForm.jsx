import React, { Component } from "react";
import Form from "react-bootstrap/Form"
import Button from "react-bootstrap/Button"
import getCommits from "./../api/getCommits"

class WelcomeForm extends Component {
    constructor(props){
        super(props);
        this.state = {
            authCode: '',
            repoOwner: '',
            repoName: ''
        }
        this.handleChangeAuthCode = this.handleChangeAuthCode.bind(this);
        this.handleChangeRepoOwner = this.handleChangeRepoOwner.bind(this);
        this.handleChangeRepoName = this.handleChangeRepoName.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChangeAuthCode(event) {
        this.setState({authCode: event.target.value});
    }
    handleChangeRepoOwner(event) {
        this.setState({repoOwner: event.target.value});
    }
    handleChangeRepoName(event) {
        this.setState({repoName: event.target.value});
    }

    handleSubmit(event){
        getCommits(this.state.repoOwner, this.state.repoName, this.state.authCode);
        const alertData = "AuthCode: " + this.state.authCode + "\nRepo Owner: " + this.state.repoOwner + "\nRepo Name: " + this.state.repoName;
        alert(alertData);
        event.preventDefault();
    }
    render() {
        return (
            
            <Form className="mx-auto my-auto" onSubmit={this.handleSubmit}>
                <Form.Group controlId="formAuthCode">
                    <Form.Label>Authentification Token</Form.Label>
                    <Form.Control placeholder="Enter Auth Token" onChange={this.handleChangeAuthCode} />
                        <Form.Text className="text-muted">
                            This isn't saved anywhere.
                        </Form.Text>
                </Form.Group>

                <Form.Group controlId="formRepoOwner">
                    <Form.Label>Repository Owner</Form.Label>
                    <Form.Control placeholder="Enter Repo Owner" onChange={this.handleChangeRepoOwner} />
                </Form.Group>
                <Form.Group controlId="formRepoName">
                    <Form.Label>Repository Name</Form.Label>
                    <Form.Control placeholder="Enter Repo Name" onChange={this.handleChangeRepoName}/>
                </Form.Group>
                <Button variant="primary" type="submit">
                    Submit
                </Button>
            </Form>
        );
    }
}

export default WelcomeForm;