package com.dino.permissions;

final class ManifestRegisterException extends RuntimeException {
    ManifestRegisterException() {
        super("No permissions are registered in the manifest file");
    }

    ManifestRegisterException(String str) {
        super(str + ": Permissions are not registered in the manifest file");
    }
}
