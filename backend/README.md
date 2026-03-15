# Backend — Spring Boot application for the Digital Signage project

### Backend Architecture

```mermaid
flowchart TD


    Entity(<b>Entity</b><br/>DB Object)

    DTO(<b>DTO</b><br/>Transfers required data only)


    A(Front End Client) <--HTTP Request---->Backend


    subgraph Backend
        direction TB
        subgraph APILayer
            B(<b>Controller</b><br/>Receives HTTP request, returns response)
            B-.->|Uses|DTO
        end
        subgraph ServiceLayer
            C(<b>Service</b><br/>Business Logic, Convert DTO to Entity and vice versa)
        end
        subgraph DatabaseLayer
            direction LR
            D(<b>Repository</b><br/>DB Queries) <-->Database(<b>Database</b>)



            D-.->|Uses|Entity
        end

        APILayer <-->ServiceLayer


        ServiceLayer <-->DatabaseLayer



    end
```


### Parallel Programming and File Structure

```mermaid
flowchart TD
    subgraph InitialSetup
        Entity1(<b>Entity</b><br/>DB Object.)
        DTO1(<b>DTO</b><br/>Transfers required data only.)
        Repository1(<b>Respository Interface</b><br/>Define database contracts. Uses Entity)
        Service1(<b>Service Interface</b><br/>Define service contracts. Uses Dto and Entity)
    end

    subgraph ParallelDevelopment
        direction LR

        subgraph APIDeveloper-controllers
            DTO2(<b>DTO</b><br/>Transfers required data only.)
            Controller2(<b>Controller</b><br/>Define api endpoints. Uses Dto)
            Controller2-.->|Uses|DTO2
        end



        subgraph BackendDeveloper-services
            Entity3(<b>Entity</b><br/>DB Object.)
            DTO3(<b>DTO</b><br/>Transfers required data only.)
            Service3(<b>Service Interface</b><br/>Define service contracts. Uses Dto and Entity)
            Service3Impl(<b>Service Implemenation</b><br/>Implements service interfaces. Define business logic. Uses Dto and Entity)

            Service3Impl-.->|Implements|Service3
            Service3Impl-.->|uses|Entity3
            Service3Impl-.->|uses|DTO3
        end



        subgraph DataDeveloper-repositories
            Entity4(<b>Entity</b><br/>DB Object.)
            Repository4(<b>Respository Interface</b><br/>Define database contracts. Uses Entity)

            Repository4-.->|uses|Entity4
        end
    end

    InitialSetup-->ParallelDevelopment

    subgraph Test-Unit-or-Integration
        subgraph APITest
            ControllerTest(<b>ControllerTest</b><br/>Test api endpoints)
        end
        subgraph BackendTest
            ServiceImplTest(<b>ServiceImplTest</b><br/>Test service implementations)

        end
        subgraph DataTest
            RepositoryTest(<b>RepositoryTest</b><br/>Test database persistency.)
        end
    end

    subgraph GithubPush
        Github(<b>Github</b><br/>Main branch)
    end

    APIDeveloper-controllers-->APITest

    BackendDeveloper-services-->BackendTest

    DataDeveloper-repositories-->DataTest

    APITest--push-->GithubPush

    BackendTest--push-->GithubPush

    DataTest--push-->GithubPush

```


**Initial Setup:**

The initial setup needs to be done first. All backend developers must agree on these setups before starting any parallel development.
-Set up the database schema (Database Entities).
-Set up Data Transfer Objects (DTOs). Not everything saved in the database needs to be exposed to the end user or developer to maintain security.
-The repository interface needs to be set up with the required contracts.
-The service interface needs to be set up with the required contracts.

**Note:** All developers (frontend and backend) need to agree on this phase. After we pass this initial setup phase, all backend development should be decoupled, meaning that throughout the parallel development process, we won't need to wait for another developer to complete their work.

**Parallel Development:**

-The API developer will develop all the controllers.
-The backend developer will develop all the services with business logic and will implement the service interface set up earlier.
-The data developer will add any custom queries if required. The amount of work here is smaller if the initial repository interface set up earlier is sufficient.

**Note:**

-The API developer can only use DTOs.
-The backend developer can use both DTOs and entities in their development.
-The data developer will only rely on the entities.

**Testing:**

Each developer will write their own test code and modular test methods to ensure everything is working as expected. There may also be a dedicated tester to write test scripts.

**Merging to GitHub Main Branch:**

Every time someone does a pull request, run all the tests! Even though some tests might not be part of the work being merged, we should run all tests to make sure that someone's development didn't break another part of the software. The same process should be followed even if there is only a single line of code change. **Run all the tests and make sure they all pass.**

## Database Architecture ERD

### Database Name: digitalsignagedb

```mermaid
erDiagram
    Device ||--|| Layout : "uses Layout. 
                        if DeviceGroup.layoutId <> NULL 
                        THEN show this layout
                        ELSE override with layout from DeviceGroup"
    DeviceGroup ||--o{ Device : has
    DeviceGroup }|--o| Layout : uses
    Layout ||--o{ LayoutSlot : contains
    LayoutSlot }o--|| Module : displays
    AdCollection ||--|{ AdContent : has
    Module ||--o| AdCollection : "has AdCollection if Module.type == ROTATING_AD"


    User {
BIGINT id pk
        VARCHAR(50) username
VARCHAR(50) email
VARCHAR(255) password
ENUM role "ADMIN"
}

DeviceGroup {
BIGINT id pk
BIGINT layoutId fk
VARCHAR(50) name
VARCHAR(255) description
DATETIME createdAt
DATETIME updatedAt
    }

Device {
BIGINT id pk
BIGINT layoutId fk
VARCHAR(50) name
VARCHAR(50) pairingId
ENUM status "ONLINE | OFFLINE"
BIGINT DeviceGroupId fk
DATETIME createdAt
DATETIME updatedAt
    }

Layout {
BIGINT id pk
VARCHAR(50) name
INT col
INT row
BIGINT DeviceGroupId fk
DATETIME createdAt
DATETIME updatedAt
}

LayoutSlot {
BIGINT id pk
BIGINT layoutId fk
BIGINT moduleId fk
INT gridCol
INT gridRow
INT colSpan
INT rowSpan
INT zIndex
}

Module {
BIGINT id pk
VARCHAR(50) name
ENUM type "CLOCK | WEATHER | ROTATING_AD"
JSON config
DATETIME createdAt
DATETIME updatedAt
}

AdCollection {
BIGINT id pk
VARCHAR(50) name
VARCHAR(255) url
DATETIME createdAt
DATETIME updatedAt
}

AdContent {
BIGINT id pk
BIGINT AdCollectionId fk
VARCHAR url(255)
ENUM type "IMAGE | VIDEO"
INT displayOrder
INT durationSeconds
}

```

## API Endpoints

### Authentication
<table>
    <tr>
        <th>Method</th>
        <th>API Endpoint</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>POST</td><td>/api/auth/login</td>
        <td>Takes <b>username</b> and <b>password</b> returns a JWT token.</td>
    </tr>
    <tr>
        <td>GET</td><td>/api/auth/user</td>
        <td>Returns the currently logged in user info based on the JWT token.</td>
    </tr>
    <tr>
        <td>POST</td><td>/api/auth/logout</td>
        <td>Destroy the JWT token</td>
    </tr>
</table>


### Device
#### Base Path /api/devices
<table>
    <tr>
        <th>Method</th>
        <th>API Endpoint</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>POST</td><td>/api/devices/register</td>
        <td>
            <ul>
                <li>Device boot first time and checks if the pairingId exists in local storage. If not, hit the endpoint</li>
                <li>Server will generate a random id and returns the id to the device.</li>
                <li>Save it in the device's local storage</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>POST</td><td>/api/devices/pair</td>
        <td>
            <ul>
                <li>Admin sees the pairingId on the device screen. Takes note of that id and enters it in the admin panel</li>
                <li>Hit this endpoint and server will register the pairingId</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>POST</td><td>/api/devices/verify-register</td>
        <td>
            <ul>
                <li>If device reboot and device has the pairingId in the local storage, hit this endpoint.</li>
                <li>Verify that the device is registered</li>
            </ul>
        </td>
    </tr>
    <tr>
        <td>GET</td><td>/api/devices/{id}/status</td>
        <td>Verify device status (online | offline).</td>
    </tr>
    <tr>
        <td>GET</td><td>/api/devices</td>
        <td>Returns list of all devices with their name, status, group.</td>
    </tr>
    <tr>
        <td>GET</td><td>/api/devices/{id}</td>
        <td>Returns a single device with their name, status, group.</td>
    </tr>
    <tr>
        <td>PATCH</td><td>/api/devices/{id}/group</td>
        <td>Reassign a device to a different group</td>
    </tr>
    <tr>
        <td>PATCH</td><td>/api/devices/{id}/status</td>
        <td>Updates device status i.e. online | offline</td>
    </tr>
    <tr>
        <td>DELETE</td><td>/api/devices/{id}</td>
        <td>Delete a device from the system.</td>
    </tr>
</table>



### DeviceGroups
#### Base Path /api/device-groups
<table>
    <tr>
        <th>Method</th>
        <th>API Endpoint</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>GET</td><td>/api/device-groups</td>
        <td>Returns all device groups with their assigned layout and device count.</td>
    </tr>
    <tr>
        <td>POST</td><td>/api/device-groups</td>
        <td>Creates a new device group</td>
    </tr>
    <tr>
        <td>GET</td><td>/api/device-groups/{id}</td>
        <td>Returns a single device group with their assigned layout and device count.</td>
    </tr>
    <tr>
        <td>PUT</td><td>/api/device-groups/{id}</td>
        <td>Updates group name or description. Assigns or unassigns a layout.</td>
    </tr>
    <tr>
        <td>DELETE</td><td>/api/device-groups/{id}</td>
        <td>Deletes the group</td>
    </tr>
</table>


### Layouts
#### Base Path /api/layouts
<table>
    <tr>
        <th>Method</th>
        <th>API Endpoint</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>GET</td><td>/api/layouts</td>
        <td>Returns all layouts</td>
    </tr>
    <tr>
        <td>POST</td><td>/api/layouts</td>
        <td>Creates a new layout.</td>
    </tr>
    <tr>
        <td>GET</td><td>/api/layouts/{id}</td>
        <td>Returns a layout and all its slots with their assigned modules. Fully resolved.</td>
    </tr>
    <tr>
        <td>PUT</td><td>/api/layouts/{id}</td>
        <td>Updates the layout info.</td>
    </tr>
    <tr>
        <td>DELETE</td><td>/api/layouts/{id}</td>
        <td>Deletes the layout.</td>
    </tr>
</table>




##### JSON Request and Response

<details>
    <summary><b>GET</b> /api/layouts/1/slots</summary>

    RESPONSE 200

    [

        {
            "id": 1,
            "name": "Campus Center Default",
            "col": 2,           // total number of columns
            "row": 1,           // total number of rows
            "deviceGroupId": 1,
            "createdAt": "2026-03-15T02:13:45:00Z",
            "updatedAt": "2026-03-15T02:13:45:00Z"
        },

        {
            "id": 2,
            "name": "Test Layout",
            "col": 2,           // total number of columns
            "row": 3,           // total number of rows
            "deviceGroupId": 1,
            "createdAt": "2026-03-15T02:13:45:00Z",
            "updatedAt": "2026-03-15T02:13:45:00Z"
        },
    ]

</details>



### Layout Slots
#### Base Path /api/layouts/{id}/slots
<table>
    <tr>
        <th>Method</th>
        <th>API Endpoint</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>GET</td><td>/api/layouts/{id}/slots</td>
        <td>Returns all layout slots</td>
    </tr>
    <tr>
        <td>POST</td><td>/api/layouts/{id}/slots</td>
        <td>Add a new layout slot</td>
    </tr>
    <tr>
        <td>PUT</td><td>/api/layouts/{id}/slots/{slotId}</td>
        <td>Updates a layout slot</td>
    </tr>
    <tr>
        <td>GET</td><td>/api/layouts/{id}/slots/{slotId}</td>
        <td>Deletes a slot from the layout.</td>
    </tr>
</table>


##### JSON Request and Response

<details>
    <summary><b>GET</b> /api/layouts/1/slots</summary>

    RESPONSE 200

    [

        {
            "id": 1,
            "layoutId": 1,
            "moduleId": 1,
            "gridCol": 1,
            "gridRow": 1,
            "colSpan": 1,
            "rowSpan": 1,
            "zIndex": 1
        },
        {
            "id": 2,
            "layoutId": 2,
            "moduleId": 2,
            "gridCol": 2,
            "gridRow": 1,
            "colSpan": 1,
            "rowSpan": 1,
            "zIndex": 1
        }

    ]

</details>


<details>
    <summary><b>POST</b> /api/layouts/1/slots</summary>

    REQUEST
    {
        "layoutId": 1,
        "moduleId": 1,
        "gridCol": 1,
        "gridRow": 1,
        "colSpan": 1,
        "rowSpan": 1,
        "zIndex": 1

    }

    RESPONSE 201

    {
        "id": 1,
        "layoutId": 1,
        "moduleId": 1,
        "gridCol": 1,
        "gridRow": 1,
        "colSpan": 1,
        "rowSpan": 1,
        "zIndex": 1
    }

</details>




<details>
    <summary><b>PUT</b> /api/layouts/1/slots/{slotId}</summary>

    REQUEST
    {
        "layoutId": 1,
        "moduleId": 2,      //updated
        "gridCol": 1,
        "gridRow": 1,
        "colSpan": 1,
        "rowSpan": 1,
        "zIndex": 1

    }

    RESPONSE 200

    {
        "id": 1,
        "layoutId": 1,
        "moduleId": 2,
        "gridCol": 1,
        "gridRow": 1,
        "colSpan": 1,
        "rowSpan": 1,
        "zIndex": 1
    }

</details>



<details>
    <summary><b>DELETE</b> /api/layouts/1/slots/{slotId}</summary>


    RESPONSE 200

    {
        "message": "Layout slot deleted successfully."
    }

</details>