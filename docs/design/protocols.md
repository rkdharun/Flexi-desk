# Flexi

Flexi enables a seamless connectivity between your devices connected in a same network.

- Allows the user to share files , clipboards
- Supports a secure transmission using encryption
- A light weight application
- supports multiple platforms

## Architecture

This application folows a client server architecture. Client scans a QR code to obtain information from the server like "ip" and "port". client intializes the connection to the server. Server will generate a QR code with "ip" and "port" informations.

## Protocol

Flexi follows TCP/IP protocols to communicate and leverages the encryption and decrytion technologies of the SSl. Since TCP/IP protocol is used, It ensures the proper transmission of data between the devices and ensures the security

## Security

Flexi uses TLS over TCP to ensure secure communication between devices. TLS provides end-to-end encryption, preventing unauthorized access to the data being transmitted. This security mechanism ensures that the content is protected from malicious attacks and other security threats, allowing for safe and secure data transmission between devices. By utilizing TLS over TCP, Flexi guarantees that the data is transmitted securely and reliably, enhancing the overall user experience.

## Packet Types

- Flexi Utilizes 4 types of packets
  - AuthenticationPacket
  - FilePacket
  - ClipBoardPacket
  - InvalidRequest

### Packet Flow

First we need to Send the authenticate Response to connecting client it can be either success or failure to accomplish this we are going to use **Authentication** Packet.

Once the server has been identified, Data is transmitted between the client and the server using either a **FilePacket** or **ClipBoardPacket** . These packets are responsible for transferring data from the client to the server and vice versa, ensuring seamless sharing of clipboard content between the two devices.

Finally we have **InvalidRequest** which is used to indicate that the packet sent by client is invalid so it provides a way to indicate that the packet status. This packet should only sent from server to client not from client to server.

#### Packet Length

The **Packet Length** field specifies the length of the packet, which is the sum of the length of the header and the length of the body. This field is used to determine the size of the packet, allowing for efficient and organized data transmission within the application. This field is First field in all of the packets.

### InvalidRequest

The **InvalidRequest** is used to indicate that the packet is invalid. This packet contains the following fields:

#### Header

- **Packet Length**: This field specifies the length of the packet, for invalid packet it is length of error code and error message.

- **Packet Type**: This field specifies the type of packet, which is set to 0x00 for the Invalid Packet.

#### Body

- **Error Code**: This field specifies the error code.
- **Error Message**: This field contains the error message.

#### Structure

| Field         | Bytes  | value |
|---------------|--------|-------|
| Packet Length | 4      |       |
| Packet Type   | 1      | 0x00  |
| Error Code    | 1      |       |
| Error Message | varies |       |

#### Possible Error Codes

| Error Code | Error Message |
|------------|---------------|
| 0x01       | Coding Error  |
| 0x02       | TLS Error     |

### Authentication

The **Authentication** is used to indicate the authentication process to the client. This packet contains the following fields:

#### Header

- **Packet Length**: This field specifies the length of the packet, for AuthPacket it is length of the auth token.
- **Packet Type**: This field specifies the type of packet, which is set to 0x01 for the AuthPacket.

#### Body

- **AuthStatus**: This field specifies the status of the authentication process. it can be one of the following values.
  - 0x00: Auth Failed
  - 0x01: Auth Success

#### Structure

| Field         | Bytes | value |
|---------------|-------|-------|
| Packet Length | 4     |       |
| Packet Type   | 1     | 0x01  |
| AuthStatus    | 1     |       |

### FilePacket

The **FilePacket** is used to transfer data between the client and the server. This packet contains the following fields:

#### Header

- **packet length** : it specifies the total length of the packet
- **packet Type** : specifies the packet type (filepacket = 0x02)

#### Body

- **FileNameLength**: This field specifies the lenght of the file name that is sent
- **FileName** : Specifies the file name with extension
- **FileDataLength**: This field specifies the size of the file in bytes .
- **FileData**: This field contains the file data.

#### Structure

| Field          | Bytes  | value |
|----------------|--------|-------|
| Packet Length  | 4      |       |
| Packet Type    | 1      | 0x02  |
| FileNameLength | 4      |       |
| FileName       | varies |       |
| FileDataLength | 4      |       |
| FileData       | varies |       |

### ClipboardPacket

The **ClipboardPacket** is used to transfer clipboard between the client and the server. This packet contains the following fields:

#### Header

- **packet length** : it specifies the total length of the packet
- **packet Type** : specifies the packet type (filepacket = 0x02)

#### Body

- **PayloadLength**: This field specifies the length of the file
- **Payload**: This field contains the file data.

#### Structure

| Field         | Bytes  | value |
|---------------|--------|-------|
| Packet Length | 4      |       |
| Packet Type   | 1      | 0x02  |
| PayloadLength | 4      |       |
| Payload       | varies |       |
